package com.softure.document_transition.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.DocumentoRelacionExpedienteSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaDineroSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaDineroDTO;
import com.softure.document_transition.domain.DocumentoRelacionGestorDTO;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.mail.application.MailGenerateMessageService;
import com.softure.notification.application.ActividadSvc;
import com.softure.notification.domain.ActividadDTO;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;
import com.softure.process_designer.infrastructure.ProcesoTransicionMapper;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.webservice.application.WebServiceExecuteAPI;

@Component
public class CallManageTransition {

	@Autowired
	@Lazy
	private DocumentoPlantillaSvc documentoService;
	@Autowired
	@Lazy
	private DocumentoRelacionGestorSvc relacionGestorService;
	@Autowired
	@Lazy
	private MailGenerateMessageService generateMessageService;
	@Autowired
	@Lazy
	private ProcesoEstadoSvc estadoService;
	@Autowired
	@Lazy
	private ProcesoTransicionSvc transicionService;
	@Autowired
	@Lazy
	private PropiedadSvc propiedadService;
	@Autowired
	@Lazy
	private PedidoVentaSvc pedidoService;
	@Autowired
	@Lazy
	private CallDocumentNewFromAutomatic createDocumentSinceProperties;
	@Autowired
	@Lazy
	private UsuarioAutenticacionSvc autenticacionService;
	@Autowired
	@Lazy
	private WebServiceExecuteAPI apiService;
	@Autowired
	@Lazy
	private ActividadSvc actividadService;
	@Autowired
	@Lazy
	private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired
	@Lazy
	private PedidoVentaDineroSvc dineroService;
	@Autowired
	@Lazy
	private ProcesoTransicionMapper procesoTransicionMapper;
	@Autowired
	@Lazy
	private RelacionInternaSvc relacionService;
	@Autowired
	@Lazy
	private DocumentoRelacionExpedienteSvc relacionExpedienteService;

	public ProcesoTransicionDTO execute(ProcesoTransicionDTO dto, String expediente, PedidoVentaDTO documentoDTO,
			BigDecimal valorModificador, PedidoVentaDineroDTO dineroProcesado,
			DocumentoRelacionGestorDTO relacionAnterior, String token, String transaccion, String previousStep)
			throws ServerException {
		String userID = getUserId(token);
		return executeInternal(dto, expediente, documentoDTO, valorModificador, dineroProcesado, relacionAnterior,
				token, transaccion, previousStep, userID, new HashMap<>(), null);
	}

	/**
	 * 
	 * @param dto
	 * @param expediente
	 * @param documentoDTO
	 * @param valorModificador
	 * @param dineroProcesado
	 * @param relacionAnterior
	 * @param token
	 * @param transaccion
	 * @param previousStep
	 * @param userID                           Para optimizar solo lo consulto la
	 *                                         primera vez
	 * @param documentRecentCreateInTransition Contiene los documentos que se van
	 *                                         creando en la ejecucion de la
	 *                                         transaccion
	 * @return
	 * @throws ServerException
	 */
	private ProcesoTransicionDTO executeInternal(ProcesoTransicionDTO dto, String expediente,
			PedidoVentaDTO documentoDTO, BigDecimal valorModificador, PedidoVentaDineroDTO dineroProcesado,
			DocumentoRelacionGestorDTO relacionAnterior, String token, String transaccion, String previousStep,
			String userID, Map<String, List<PedidoVentaDTO>> documentRecentCreateInTransition,
			String locationTransition) throws ServerException {

		// Aqui lleno las propiedades del dto asi no falla api
		if (dto.getPropiedades() == null)
			dto.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION,
					dto.getLlaveTabla(), null, userID));
		propiedadService.validarFuncionConsultandoPropiedad(dto, PropiedadValorDefinidoDTO.TRANSICION, expediente,
				documentoDTO.getLlaveTabla(), userID);
		ProcesoTransicionDTO respuesta = dto;
		PedidoVentaDTO expedienteDTO = pedidoService.consultaXId(expediente);
		ProcesoEstadoDTO filtroEstado = estadoService.consultaXId(dto.getEstadoLLegada());
		ProcesoEstadoDTO anteriorEstado = null;
		if (dto.getEstadoPartida() != null)
			anteriorEstado = estadoService.consultaXId(dto.getEstadoPartida());
		if (filtroEstado == null)
			throw new ServerException(
					"No se encuentra estado de llegada, en caso que no se modifiquen coloque el mismo estado.\n"
							+ expedienteDTO.getNombre() + " - " + expedienteDTO.getDescripcion());
		System.out.format("\n\n[%s] Procesando transicion (%s) del proceso (%s)", expedienteDTO.getNombre(),
				dto.getNombre(), dto.getProcesoNombre());
		String modificadorId = null;
		PedidoVentaDineroDTO afectado = null;
		// Estos documentos se crean en la transicion y deben ser procesados por el
		// momento en el api

		String nameTrace = (previousStep == null) ? dto.getNombre() : previousStep + "->" + dto.getNombre();
		if (anteriorEstado != null && anteriorEstado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ITERADOR) == 0) {
			afectado = iterateInState(respuesta, expedienteDTO, documentoDTO, token, relacionAnterior,
					documentRecentCreateInTransition, dineroProcesado);
		} else {
			String ubicacion = obtenerUbicacion(documentoDTO, dto.getLlaveTabla(), token);
			if (ubicacion != null)
				locationTransition = ubicacion;
			modificadorId = documentoDTO.getLlaveTabla();
			// Genero documento en caso que toque
			if (dto.getPlantilla() != null) {
				String tokenToGenerateDocument = token;
				// En caso de los apis si no habia colocado el permiso fallaba por ese permiso
				// pero el api se enviaba asi que peligro porque terminaba haciend varias veces
				// lo mismo Varios SMS, Varios Manifiestos
				if (anteriorEstado != null && anteriorEstado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_API) == 0)
					tokenToGenerateDocument = autenticacionService.generateAdministratorToken().getLlaveTabla();
				// Tengo que optimizar esto siempre va a preguntar si tiene documentos para
				// generar
				PedidoVentaDTO automatico = createDocumentSinceProperties.generateDocuments(dto, documentoDTO,
						expedienteDTO, documentoDTO.getTransaccion(), tokenToGenerateDocument, 0,
						documentRecentCreateInTransition, null);
				// Por si es la transicion inicial no le quite el poder del documento que genero
				if (automatico != null) {
					if (automatico.getPlantilla().compareTo(dto.getPlantilla()) == 0)
						modificadorId = automatico.getLlaveTabla();

					if (automatico.getDinero() != null && automatico.getDinero().getValorTotal() != null)
						valorModificador = automatico.getDinero().getValorTotal();
				}

			}
			// movi esto despues de la creacion de la plantilla para que tome el valor
			// modificador del nuevo documento creado
			System.out.format("\n[%s] Afectando saldos con parametro de la transicion %s", expedienteDTO.getNombre(),
					dto.getAfectaSaldo());
			afectado = moveBalanceDocument(expediente, token, dto, valorModificador, dineroProcesado);

			System.out.format("\n[%s] Envia a motor de traza por modificador ( %s ) ", expedienteDTO.getNombre(),
					documentoDTO.getNombre());
			// Creo la relacion del documento Gestor
			relacionAnterior = relacionGestorService.trazar(expedienteDTO.getLlaveTabla(), modificadorId, nameTrace,
					dto.getEstadoPartida(), dto.getEstadoLLegada(),
					(afectado == null) ? null : afectado.getLlaveTabla(), locationTransition, token, relacionAnterior,
					expedienteDTO.getHistorico(), transaccion, false);
		}
		// Se actualiza pedido
		// si son los mismo creo que no necesito update ???????????
		System.out.format("\n[%s] Se actualiza estado del documento de ( %s ) a ( %s )", expedienteDTO.getNombre(),
				expedienteDTO.getEstadoNombre(), filtroEstado.getNombre());
		expedienteDTO.setEstadoExpediente(filtroEstado.getLlaveTabla());
		expedienteDTO.setEstadoNombre(filtroEstado.getNombre());
		// No se porque tenia esta linea
		// ->//anterior.setEstadoNombre(filtroEstado.getNombre());
		expedienteDTO.setEstado(filtroEstado.getEstadoDocumento());
		switch (dto.getEstadoLlegadaTipo()) {
		case ProcesoEstadoDTO.TIPO_DECISION:
			respuesta = resolveStateDesition(dto.getEstadoLLegada(), expediente, documentoDTO.getLlaveTabla(), token);
			UsuarioSesionDTO tokenSystem = autenticacionService.generateAdministratorToken();
			//Aqui clean los documentos creados se supone que ya se tuv o que hacer lo de la iteracion
			respuesta = executeInternal(respuesta, expediente, documentoDTO, valorModificador, afectado,
					relacionAnterior, tokenSystem.getLlaveTabla(), transaccion, nameTrace, userID,
					new HashMap<>(), locationTransition);
			break;
		case ProcesoEstadoDTO.TIPO_ITERADOR:
			respuesta = getNextTransition(dto.getEstadoLLegada(), null);
			// Por si siguen decisiones
			respuesta = executeInternal(respuesta, expediente, documentoDTO, valorModificador, afectado,
					relacionAnterior, token, transaccion, nameTrace, userID, documentRecentCreateInTransition,
					locationTransition);
			// Aqui tambien gestiona mensajes se duplica porque no evalue bien que eimpato
			// tiene ponerlo antes o despues
			generateMessageService.call(expedienteDTO, dto, null, documentoDTO, token);
			break;
		case ProcesoEstadoDTO.TIPO_API:
			respuesta = executeAPI(dto.getEstadoLLegada(), expedienteDTO, documentoDTO, token,
					documentRecentCreateInTransition);
			try {
				// Por si siguen decisiones
				respuesta = executeInternal(respuesta, expediente, documentoDTO, valorModificador, afectado,
						relacionAnterior, token, transaccion,
						(previousStep == null) ? dto.getEstadoLlegadaNombre()
								: previousStep + "->" + dto.getEstadoLlegadaNombre(),
						userID, documentRecentCreateInTransition, locationTransition);
			} catch (Exception e) {
				CallDocumentCommons.addMessageError(documentoDTO, e.getMessage());
			}

			break;
		default:
			// No entiendo el motivo pero este update se tiene que dejar aqui
			// en logimax hay una transcicion que cambia la cantidad y despues itera si no
			// dejo aqui este update el estado no quedaba correcto quedaba en la iteracion
			pedidoService.update(expedienteDTO);
			UsuarioDTO responsable = assignResponsibleToActivity(expediente, filtroEstado.getLlaveTabla(),
					filtroEstado.getNombre(), documentoDTO.getLlaveTabla(), token);
			generateMessageService.call(expedienteDTO, dto, responsable, documentoDTO, token);
			activateHistoric(expedienteDTO);
			break;
		}

		return respuesta;
	}

	private void activateHistoric(PedidoVentaDTO expedienteDTO) {
		if (expedienteDTO == null)
			return;
		if (expedienteDTO.getHistorico() != null
				&& expedienteDTO.getEstado().compareTo(SharedConstants.STATE_ACTIVE) == 0) {
			procesoTransicionMapper.funcionRegresarTablaHistoricos(expedienteDTO.getLlaveTabla());
		}
	}

	/**
	 * 
	 * @param pTransition                        Estado que contine la iteracion y
	 *                                           donde vamos a buscar al funcion
	 * @param pDocumentPrincipal                 Documento principal que se esta
	 *                                           iterDocumento Proceso que estamos
	 *                                           afectando
	 * @param pDocumentoModificador              Documento que realizo la acción y
	 *                                           disparo la transicion
	 * @param pToken                             Codigo de seguridad de la
	 *                                           transaccion
	 * @param pRelationBack                      SE necesita para la traza :(
	 * @param pStackDocumentsCreateInTransaction Se necesita para la traza :(
	 * @return
	 * @throws ServerException
	 */
	// aqui  hay algo  para  mejorar
	private PedidoVentaDineroDTO iterateInState(ProcesoTransicionDTO pTransition, PedidoVentaDTO pDocumentPrincipal,
			PedidoVentaDTO pDocumentoModificador, String pToken, DocumentoRelacionGestorDTO pRelationBack,
			Map<String, List<PedidoVentaDTO>> pStackDocumentsCreateInTransaction, PedidoVentaDineroDTO dineroProcesado 
	) throws ServerException {

		PedidoVentaDineroDTO afectado = null;
		ProcesoEstadoDTO _stateInitial = estadoService.consultaXId(pTransition.getEstadoPartida());
		if (_stateInitial.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("La iteracion " + _stateInitial.getNombre() + " esta inactiva");
		PropiedadDTO _propertyFuncionSQL = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO,
				_stateInitial.getLlaveTabla(), Propiedades.ITERACION_SQL, null);
		List<PedidoVentaDTO> _documentsToCreate = null;
		if (_propertyFuncionSQL == null) {
			if (_propertyFuncionSQL == null)
				throw new ServerException(
						"La iteracion " + _stateInitial.getNombre() + " no tiene definida la funcion SQL");

		} else {
			try {
				_documentsToCreate = pedidoService.iteracionesProceso(
						SoftureUtil.formatFunction(_propertyFuncionSQL.getLlaveTabla()),
						pDocumentPrincipal.getLlaveTabla(),
						(pDocumentoModificador == null) ? null : pDocumentoModificador.getLlaveTabla());
			} catch (Exception e) {
				throw new ServerException(e.getMessage(), "Iteracion : " + _stateInitial.getNombre());
			}
		}
		List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(_propertyFuncionSQL.getLlaveTabla());
		if (relaciones == null || relaciones.isEmpty()) {
			// no se si esto
			if(pStackDocumentsCreateInTransaction.get(pTransition.getLlaveTabla())==null) {
				pStackDocumentsCreateInTransaction.put(pTransition.getLlaveTabla(), _documentsToCreate);	
			}else {
				pStackDocumentsCreateInTransaction.get(pTransition.getLlaveTabla()).addAll(_documentsToCreate);
			}
		} else {
			if (_documentsToCreate != null && !_documentsToCreate.isEmpty()) {
				for (RelacionInternaDTO _iRelacion : relaciones) {
					if(pStackDocumentsCreateInTransaction.get(_iRelacion.getCampo())==null) {
						pStackDocumentsCreateInTransaction.put(_iRelacion.getCampo(), _documentsToCreate);	
					}else {
						pStackDocumentsCreateInTransaction.get(_iRelacion.getCampo()).addAll(_documentsToCreate);
					}
				}
			}
		}
		if (pTransition.getPlantilla() != null) {
			if (_documentsToCreate != null && !_documentsToCreate.isEmpty()) {
				List<PedidoVentaDTO> _result = new ArrayList<>();
				for (int i = 0; i < _documentsToCreate.size(); i++) {
					PedidoVentaDTO iDocumentoIterar = _documentsToCreate.get(i);
					iDocumentoIterar.setCaracteristicas(pedidoVentaCaracteristicaService
							.listar2Documento(iDocumentoIterar.getLlaveTabla(), iDocumentoIterar.getHistorico()));
					// Aqui al parecer el expediednte principal es el modificador pero no me parece
					// que sea asi, deberia ser el expediente??, o talvez todos
					PedidoVentaDTO _newDocumentOfIteration = createDocumentSinceProperties.generateDocuments(pTransition,
							pDocumentoModificador, pDocumentPrincipal, iDocumentoIterar.getTransaccion(), pToken, i + 1,
							pStackDocumentsCreateInTransaction, iDocumentoIterar);
					// Creo la relacion del documento Gestor
					relacionGestorService.trazar(pDocumentPrincipal.getLlaveTabla(),
							(_newDocumentOfIteration == null) ? null : _newDocumentOfIteration.getLlaveTabla(), pTransition.getNombre(),
							pTransition.getEstadoPartida(), pTransition.getEstadoLLegada(), null, null, pToken,
							pRelationBack, pDocumentPrincipal.getHistorico(), null, false);
					if (_newDocumentOfIteration != null) {
						_result.add(_newDocumentOfIteration);
						// Esto es porque cuando son iteradores no se gestionaba el dinero
						if (_newDocumentOfIteration.getDinero() != null && _newDocumentOfIteration.getDinero().getSaldo() != null)
							afectado = moveBalanceDocument(pDocumentPrincipal.getLlaveTabla(), pToken, pTransition,
									_newDocumentOfIteration.getDinero().getValorTotal(), null);
						PropiedadDTO _propertyAgreggate = propiedadService.obtenerPropiedad(
								PropiedadValorDefinidoDTO.ESTADO, _stateInitial.getLlaveTabla(),
								Propiedades.ADD_ITERATION_DOCUMENT, null);
						if (_propertyAgreggate != null) {
							List<RelacionInternaDTO> _relationToAdd = relacionService
									.relacionesPropiedad(_propertyAgreggate.getLlaveTabla());
							if (_relationToAdd == null || _relationToAdd.isEmpty()) {
								throw new ServerException("La propiedad " + _propertyAgreggate.getNombre()
										+ " de la iteracion " + pTransition.getNombre() + " del proceso "
										+ pTransition.getProcesoNombre() + " con estado inicial "
										+ pTransition.getEstadoPartidaNombre()
										+ ", no tiene relaciones, usa las relaciones para identificar que campo deseas utilizar");
							} else {
								//En comporbante de egreso necesito tener las conciliraciones 1x1 para el comprobante contable
								// aun asi puedo relacionar con un campo de el principal del modificador o de uno que se va a crear
								// ejemplo en rodamiento al legalizar
								List<PedidoVentaCaracteristicaDTO> _fieldsToAdd = new ArrayList<>();
								if (pDocumentoModificador != null && pDocumentoModificador.getCaracteristicas()!=null)
									_fieldsToAdd.addAll(pDocumentoModificador.getCaracteristicas());
								if (pDocumentPrincipal != null && pDocumentPrincipal.getCaracteristicas()!=null)
									_fieldsToAdd.addAll(pDocumentPrincipal.getCaracteristicas());
								
								for (RelacionInternaDTO _iRelacion : _relationToAdd) {
									boolean _found = false;
									for(PedidoVentaCaracteristicaDTO _iFieldDocumentPrincipal : _fieldsToAdd) {
										if (_iFieldDocumentPrincipal.getCampo()
												.compareTo(_iRelacion.getCampo()) == 0) {
											relacionExpedienteService.relacionarExpedienteDocumento(_iFieldDocumentPrincipal.getLlaveTabla(),
													_newDocumentOfIteration.getLlaveTabla(), pToken, _iRelacion.getCampoNombre(),
													_newDocumentOfIteration.getTransaccion(),
													(_newDocumentOfIteration.getDinero() != null) ? _newDocumentOfIteration.getDinero().getSaldo()
															: null);
											_found = true;
											break;
										}
									}
									if (!_found) {
										if(pStackDocumentsCreateInTransaction.get(_iRelacion.getCampo())==null) {
											List<PedidoVentaDTO> _list = new ArrayList<>();
											_list.add(_newDocumentOfIteration);
											pStackDocumentsCreateInTransaction.put(_iRelacion.getCampo(), _list);	
										}else {
											pStackDocumentsCreateInTransaction.get(_iRelacion.getCampo()).add(_newDocumentOfIteration);
										}
									}
								}
							}
						} else {
							if(pStackDocumentsCreateInTransaction.get(pTransition.getLlaveTabla())==null) {
								List<PedidoVentaDTO> _list = new ArrayList<>();
								_list.add(_newDocumentOfIteration);
								pStackDocumentsCreateInTransaction.put(pTransition.getLlaveTabla(), _list);	
							}else {
								pStackDocumentsCreateInTransaction.get(pTransition.getLlaveTabla()).add(_newDocumentOfIteration);
							}
						}
					}
				}
				if (_result.size() == 0)
					throw new ServerException(
							"No se generaron documentos en la iteracion revisa las propiedades de la transicion para crear los campos");
			}
		}
		return afectado;
	}

	private ProcesoTransicionDTO executeAPI(String estadoLlegada, PedidoVentaDTO expedienteDTO,
			PedidoVentaDTO documentoDTO, String token,
			Map<String, List<PedidoVentaDTO>> documentRecentCreateInTransition) throws ServerException {
		ProcesoEstadoDTO apiDTO = estadoService.consultaXId(estadoLlegada);
		if (apiDTO.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El punto del api " + apiDTO.getNombre() + " esta inactivo");
		apiDTO.setPropiedades(propiedadService.obtenerPropiedadesSinEntidad(PropiedadValorDefinidoDTO.ESTADO,
				estadoLlegada, null, getUserId(token)));

		PropiedadDTO propAPI = Propiedades.obtenerParametro(apiDTO, Propiedades.API);
		if (propAPI == null)
			throw new ServerException("El estado %s no tiene definido el API".formatted(apiDTO.getNombre()));

		String resultAPI = SharedConstants.OK;
		if (documentRecentCreateInTransition == null || documentRecentCreateInTransition.isEmpty()) {
			resultAPI = apiService.prepareApiToExecution(propAPI.getValor(), expedienteDTO, documentoDTO, null, token,
					apiService.prepareParameterFromProperties(null,
							Propiedades.obtenerVariosParametro(apiDTO, Propiedades.API_PARAMETER)));
		} else {
			// Para el manifiesto primero se crea muchas remesas y despues un solo
			// manifiesto
			PropiedadDTO propOneExecution = Propiedades.obtenerParametro(apiDTO,
					Propiedades.API_ITERATION_ONE_EXECUTION);
			if (propOneExecution != null) {
				String stringToDocumentsToAPI = "";

				for (Map.Entry<String, List<PedidoVentaDTO>> entry : documentRecentCreateInTransition.entrySet()) {
					for (int i = 0; i < entry.getValue().size(); i++) {
						stringToDocumentsToAPI = stringToDocumentsToAPI + SharedConstants.PUNTO_COMA_DOBLE
								+ "ITERADOR_CODE[" + i + "]" + SharedConstants.IGUAL
								+ entry.getValue().get(i).getNombre();
					}
				}

				resultAPI = apiService.prepareApiToExecution(propAPI.getValor(), expedienteDTO, documentoDTO, null, token,
						stringToDocumentsToAPI);
			} else {
				// en caso de error solo ejecuto en la proxima trnsaccion los que fueron
				// exitosos
				List<PedidoVentaDTO> okDocumentsInAPI = new ArrayList<>();
				for (Map.Entry<String, List<PedidoVentaDTO>> entry : documentRecentCreateInTransition.entrySet()) {
					for (int i = 0; i < entry.getValue().size(); i++) {
						PedidoVentaDTO pedidoVentaDTO = entry.getValue().get(i);
						resultAPI = apiService.prepareApiToExecution(propAPI.getValor(), expedienteDTO, pedidoVentaDTO, pedidoVentaDTO,
								token, SharedConstants.PUNTO_COMA_DOBLE + "ITERADOR_NUMBER" + SharedConstants.IGUAL + i);
						if (resultAPI.compareTo(SharedConstants.OK) != 0) {
							// Esto es
							// documentRecentCreateInTransition = okDocumentsInAPI;
							if (!okDocumentsInAPI.isEmpty())
								resultAPI = SharedConstants.INCOMPLETE;
							break;
						}
						okDocumentsInAPI.add(pedidoVentaDTO);
					}
				}
			}

		}
		return getNextTransition(estadoLlegada, resultAPI);
	}

	private ProcesoTransicionDTO resolveStateDesition(String decision, String llaveTablaDocumento,
			String llaveModificador, String token) throws ServerException {
		ProcesoEstadoDTO decisionDTO = estadoService.consultaXId(decision);
		if (decisionDTO.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("La decision " + decisionDTO.getNombre() + " esta inactiva");
		PropiedadDTO propiedadFuncion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO, decision,
				Propiedades.DECISION_SQL, getUserId(token));
		String resultado = null;
		if (propiedadFuncion == null) {
			resultado = "OK";
		} else {
			try {
				// ramdom por problemas del framework se repetia la respuesta cuando iteraba
				resultado = procesoTransicionMapper.decision(
						SoftureUtil.formatFunction(propiedadFuncion.getLlaveTabla()), llaveTablaDocumento,
						llaveModificador, estadoService.generarLlave());
			} catch (Exception e) {
				throw new ServerException(e.getMessage(), "Decision : " + decisionDTO.getNombre());
			}
			// Antes tenia esto como una excepcion pero para los apis asincronos eso no
			// iporta tanto
			if (resultado == null)
				resultado = "ERROR";
			// throw new ServerException("El resultado ha sido nulo\nDecision : " +
			// decisionDTO.getNombre());
		}
		ProcesoTransicionDTO solucion = getNextTransition(decisionDTO.getLlaveTabla(), resultado);
		return solucion;
	}

	private ProcesoTransicionDTO getNextTransition(String estadoActual, String nombreTransicion)
			throws ServerException {
		ProcesoTransicionFilterDTO solucionFilter = new ProcesoTransicionFilterDTO();
		solucionFilter.setEstadoPartida(estadoActual);
		solucionFilter.setNombre(nombreTransicion);
		solucionFilter.setEstado(SharedConstants.STATE_ACTIVE);
		List<ProcesoTransicionDTO> soluciones = transicionService.listarConsulta(solucionFilter);
		if (soluciones != null && !soluciones.isEmpty()) {
			if (soluciones.size() > 1)
				throw new ServerException("En el proceso " + soluciones.get(0).getProcesoNombre() + " en el estado "
						+ soluciones.get(0).getEstadoPartidaNombre()
						+ " existen mas de una relacion que cumple con el nombre " + soluciones.get(0).getNombre());
			return soluciones.get(0);
		}

		// La idea es evitar que se pierda informacion enn las apis ya que no se guarda
		// los archivos
		solucionFilter.setNombre(SharedConstants.OK);
		ProcesoTransicionDTO solucion = transicionService.consultaUnica(solucionFilter);
		if (solucion != null)
			return solucion;
		// Como primero lo hice con OK la idea es cambiar a default
		solucionFilter.setNombre(SharedConstants.DEFAULT);
		solucion = transicionService.consultaUnica(solucionFilter);
		if (solucion != null)
			return solucion;
		ProcesoEstadoDTO decisionDTO = estadoService.consultaXId(estadoActual);
		String msgException = "La decision " + decisionDTO.getNombre() + " del proceso "
				+ decisionDTO.getProcesoNombre() + " esta intentando buscar un camino para la respuesta ( "
				+ nombreTransicion + " ), actualmente ";
		solucionFilter.setNombre(null);
		List<ProcesoTransicionDTO> responseTransition = transicionService.listarConsulta(solucionFilter);
		if (responseTransition != null && !responseTransition.isEmpty()) {
			msgException += " se tiene configurado respuesta para : ";
			for (int i = 0; i < responseTransition.size(); i++) {
				msgException += "\n " + String.valueOf(i + 1) + " - " + responseTransition.get(i).getNombre();
			}
			msgException += " \nrevisa la propiedad SQL de la decision";
		} else {
			msgException += " no tienes configurada ninguna respuesta";
		}
		throw new ServerException(msgException);
	}

	private String getUserId(String token) throws ServerException {
		return transicionService.getUserFlex(token);
	}

	public UsuarioDTO assignResponsibleToActivity(String pedido, String estado, String estadoNombre, String modificador,
			String token) throws ServerException {// , DocumentoPlantillaDTO plantilla
		if (estado == null)
			return null;
		ActividadDTO responsable = new ActividadDTO();
		String userID = getUserId(token);
		PropiedadDTO propiedadFuncion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO, estado,
				Propiedades.FUNCION_SQL_ESTADO_ASIGNAR, userID);
		if (propiedadFuncion != null) {
			responsable.setResponsable(estadoService.obtenerResponsable(propiedadFuncion, pedido, modificador, token));
		} else {
			propiedadFuncion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO, estado,
					Propiedades.ESTADO_ASIGNAR, userID);
			if (propiedadFuncion != null) {
				responsable.setResponsable(propiedadFuncion.getValor());
			} else {
				// retire la plantilla
				/*
				 * String campoResponsable = ""; if(plantilla!=null) campoResponsable =
				 * Propiedades.obtenerValor(plantilla, Propiedades.RESPONSABLE);
				 * if(!campoResponsable.isEmpty()){ PedidoVentaCaracteristicaDTO campoValor=
				 * pedidoService.obtenerValor(pedido.getCaracteristicas(), campoResponsable);
				 * if(campoValor==null) throw new
				 * ServerException("Se debe colocar la caracteristica de responsable");
				 * 
				 * responsable.setResponsable(obtenerUsuarioDocumento(campoValor.getValorOpcion(
				 * ))); }else{ responsable.setResponsable(null); }
				 */
				// Esto lo coloque porque en ese estado se supone que ya no debe quedar a nombre
				// de nadie por eso se debe borrar
				responsable.setResponsable(null);
			}
		}
		responsable.setDocumento(pedido);
		responsable.setComentario(estadoNombre);
		return actividadService.crearActividad(responsable, token);
	}

	private PedidoVentaDineroDTO moveBalanceDocument(String expediente, String securityToken,
			ProcesoTransicionDTO transicion, BigDecimal saldoDocumento, PedidoVentaDineroDTO dineroDocumentoInicial)
			throws ServerException {
		PedidoVentaDineroDTO dinero = dineroDocumentoInicial;
		PedidoVentaDTO pExpediente = pedidoService.consultaXId(expediente);
		if (dinero == null) {
			dinero = dineroService.consultaPorDocumento(expediente, pExpediente.getHistorico(),
					pExpediente.getNombre());
		}

		if (transicion.getAfectaSaldo() == null)
			return dinero;
		if (dinero == null) {
			throw new ServerException("Revise el documento " + pExpediente.getNombre()
					+ " porque no tiene ningun registro de valores de saldos");
		}
		if (saldoDocumento == null)
			throw new ServerException("Revise porque el documento no tiene saldo. La transicion "
					+ transicion.getNombre() + " del proceso " + transicion.getProcesoNombre() + " solicita un valor");

		BigDecimal factor = BigDecimal.ONE;
		if (transicion.getAfectaSaldo().compareTo(ProcesoTransicionDTO.RESTANDO) == 0)
			factor = factor.negate();

		BigDecimal _calculateNewSaldo = dinero.getSaldo().add(saldoDocumento.multiply(factor));

		System.out.format("\n" + transicion.getNombre() + " [" + pExpediente.getNombre() + "] : " + dinero.getSaldo()
				+ " + " + saldoDocumento.multiply(factor) + " = " + _calculateNewSaldo);
		if (transicion.getEstadoPartida() == null) { // Para los documentos iniciales
			if (transicion.getAfectaSaldo().compareTo(ProcesoTransicionDTO.SUMANDO) != 0)
				throw new ServerException("No es logico que inicie in proceso restando");
			dinero.setSaldo(_calculateNewSaldo);
			// Controlar saldo se coloca para poder controlar los cambios de valores dle
			// documento en el tiempo con modificar
			dinero.setControlarSaldo(true);
			validateSaldo(transicion, saldoDocumento, dinero, pExpediente);
			dineroService.update(dinero);// Se acaba de crear siempre va a ser tabla productiva
			return dinero;
		}
		dineroService.inactivarConHistorial(dinero, pExpediente.getHistorico());
		PedidoVentaDineroDTO nuevo = new PedidoVentaDineroDTO();
		nuevo.setSaldo(_calculateNewSaldo);
		// Controlar saldo se coloca para poder controlar los cambios de valores dle
		// documento en el tiempo con modificar
		nuevo.setControlarSaldo(true);
		nuevo.setDocumento(dinero.getDocumento());
		nuevo.setValorTotal(dinero.getValorTotal());
		validateSaldo(transicion, saldoDocumento, nuevo, pExpediente);
		return dineroService.guardarConHistorial(nuevo, pExpediente.getHistorico());
	}

	private void validateSaldo(ProcesoTransicionDTO transicion, BigDecimal saldoDocumento,
			PedidoVentaDineroDTO saldosCalculados, PedidoVentaDTO pExpediente) throws ServerException {
		// System.out.format(
		// "\n" + transicion.getNombre() + " (" + pExpediente.getNombre() + " : " +
		// saldosCalculados.getValorTotal()
		// + ")" + saldosCalculados.getSaldo() + " - " + saldoDocumento + " = " +
		// saldosCalculados.getSaldo());
		if (saldosCalculados.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServerException(
					transicion.getNombre() + " (" + documentoService.consultaXId(pExpediente.getPlantilla()).getNombre()
							+ " " + pExpediente.getNombre() + " : Por un total de "
							+ SoftureUtil.formatMoney(saldosCalculados.getValorTotal()) + ")\n\n Saldos "
							+ SoftureUtil.formatMoney(saldosCalculados.getSaldo().add(saldoDocumento)) + " - "
							+ SoftureUtil.formatMoney(saldoDocumento) + " = "
							+ SoftureUtil.formatMoney(saldosCalculados.getSaldo()));
		}
		if (saldosCalculados.getSaldo().compareTo(saldosCalculados.getValorTotal()) > 0) {
			throw new ServerException("Revise porque el saldo del documento es mayor al valor total.\nDocumento: "
					+ pExpediente.getNombre() + "\nSaldo: " + SoftureUtil.formatMoney(saldosCalculados.getSaldo())
					+ "\nTotal: " + SoftureUtil.formatMoney(saldosCalculados.getValorTotal()));
		}
	}

	public String obtenerUbicacion(PedidoVentaDTO pedido, String transicion, String token) throws ServerException {
		if (transicion == null)
			return null;
		PropiedadDTO ubicacion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.TRANSICION, transicion,
				Propiedades.UBICACION, getUserId(token));
		if (ubicacion == null)
			return null;
		System.out.format("\n......Buscando ubicacion del documento %s", pedido.getNombre());
		PedidoVentaCaracteristicaDTO campoValor = CallDocumentCommons.obtenerValor(pedido.getCaracteristicas(),
				ubicacion.getValor());
		if (campoValor == null)
			throw new ServerException(
					"Revisa la configuracion de ubicacion, el campo ya no esta disponible. " + ubicacion.getTexto());
		return campoValor.getValorOpcion();
	}

	/*
	 * Esto es lo mimo de la normal pero vuelve al estado incial, tengo que ver como
	 * cambio esto
	 */
	public ProcesoTransicionDTO gestionarTransicionReversa(ProcesoTransicionDTO dto, String expediente,
			PedidoVentaDTO documento, String token) throws ServerException {
		ProcesoTransicionDTO respuesta = dto;
		PedidoVentaDTO anterior = pedidoService.consultaXId(expediente);
		ProcesoEstadoDTO filtroEstado = estadoService.consultaXId(dto.getEstadoPartida());
		if (filtroEstado == null)
			throw new ServerException(
					"No se encuentra estado de partida, en caso que no se modifiquen coloque el mismo estado.\n"
							+ anterior.getNombre() + " - " + anterior.getDescripcion());
		if (filtroEstado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ESTADO) != 0)
			throw new ServerException("No se puede devolver a una decision");
		String ubicacion = obtenerUbicacion(documento, dto.getLlaveTabla(), token);
		BigDecimal valorModificador = null;
		if (dto.getAfectaSaldo() != null) {
			if (dto.getAfectaSaldo().compareTo(ProcesoTransicionDTO.RESTANDO) == 0) {
				dto.setAfectaSaldo(ProcesoTransicionDTO.SUMANDO);
			} else {
				dto.setAfectaSaldo(ProcesoTransicionDTO.RESTANDO);
			}
			valorModificador = procesoTransicionMapper.valorEntransicionParaRevertir(documento.getLlaveTabla(),
					expediente);
		}
		// aqui es nulo porque ya existe
		PedidoVentaDineroDTO nuevoValor = moveBalanceDocument(expediente, token, dto, valorModificador, null);
		// Creo la relacion del documento Gestor
		relacionGestorService.trazar(anterior.getLlaveTabla(), documento.getLlaveTabla(), dto.getNombre(),
				dto.getEstadoLLegada(), dto.getEstadoPartida(),
				(nuevoValor == null) ? null : nuevoValor.getLlaveTabla(), ubicacion, token, null,
				anterior.getHistorico(), documento.getTransaccion(), false);
		// Se actualiza pedido
		System.out.println(
				anterior.getNombre() + " : " + filtroEstado.getNombre() + "(" + anterior.getEstadoNombre() + ")");
		anterior.setEstadoExpediente(filtroEstado.getLlaveTabla());
		anterior.setEstado(filtroEstado.getEstadoDocumento());
		// No se porque tenia esta
		// linea//anterior.setEstadoNombre(filtroEst+*ado.getNombre());
		pedidoService.update(anterior);
		assignResponsibleToActivity(expediente, filtroEstado.getLlaveTabla(), filtroEstado.getNombre(),
				documento.getLlaveTabla(), token);
		// Por el momento asumo que no tuvo preguntas
		/*
		 * if(dto.getEstadoPartid().compareTo(ProcesoEstadoDTO.TIPO_DECISION)==0) {
		 * respuesta= decision(dto.getEstadoLLegada(), expediente,
		 * documento.getLlaveTabla()); //Aqui coloco la traza de las decisiones me falta
		 * unirlas, las otras se gestionan en cada parte por el dinero
		 * //relacionGestorService.trazar(documento, expediente, dto.getEstadoLLegada(),
		 * respuesta.getEstadoPartida(), nuevoValor, ubicacion, dto.getSecurityToken());
		 * respuesta = gestionarTransicion(respuesta, expediente, documento,
		 * nuevoValor); }else {
		 * 
		 * //Quito los mensajes se supone que devuelve
		 * //mensajeSvc.gestionarMensajes(anterior, dto, responsable, documento); }
		 */
		return respuesta;
	}

}
