package com.softure.document_transition.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaDineroSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaDineroDTO;
import com.softure.document_transition.domain.DocumentoRelacionGestorDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
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
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.webservice.application.WebServiceExecuteAPI;

@Component
public class CallManageTransition {

	@Autowired
	private DocumentoPlantillaSvc documentoService;
	@Autowired
	private DocumentoRelacionGestorSvc relacionGestorService;
	@Autowired 
	private MailGenerateMessageService generateMessageService;
	@Autowired
	private ProcesoEstadoSvc estadoService;
	@Autowired
	private ProcesoTransicionSvc transicionService;
	@Autowired
	private PropiedadSvc propiedadService;
	@Autowired
	private PedidoVentaSvc pedidoService;
	@Autowired
	private CallDocumentNewFromAutomatic createDocumentSinceProperties;
	@Autowired
	private UsuarioAutenticacionSvc autenticacionService;
	@Autowired
	private WebServiceExecuteAPI apiService;
	@Autowired
	private ActividadSvc actividadService;
	@Autowired
	private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired
	private PedidoVentaDineroSvc dineroService;
	@Autowired
	private ProcesoTransicionMapper procesoTransicionMapper;

	
	
	public ProcesoTransicionDTO execute(ProcesoTransicionDTO dto, String expediente, PedidoVentaDTO documentoDTO,
			BigDecimal valorModificador, PedidoVentaDineroDTO dineroProcesado,
			DocumentoRelacionGestorDTO relacionAnterior, String token, String transaccion, String previousStep) throws ServerException {
		String userID = getUserId(token);
		return executeInternal(dto, expediente, documentoDTO, valorModificador, dineroProcesado, relacionAnterior, token, transaccion, previousStep, userID, null);
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
	 * @param userID								Para optimizar solo lo consulto la primera vez 
	 * @param documentRecentCreateInTransition		Contiene los documentos que se van creando en la ejecucion de la transaccion
	 * @return
	 * @throws ServerException
	 */
	private ProcesoTransicionDTO executeInternal(ProcesoTransicionDTO dto, String expediente, PedidoVentaDTO documentoDTO,
			BigDecimal valorModificador, PedidoVentaDineroDTO dineroProcesado,
			DocumentoRelacionGestorDTO relacionAnterior, String token, String transaccion, String previousStep, String userID, List<PedidoVentaDTO> documentRecentCreateInTransition) throws ServerException {

		
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
		//Estos documentos se crean en la transicion y deben ser procesados por el momento en el api
		
		String nameTrace = (previousStep==null)?dto.getNombre(): previousStep + "->" +dto.getNombre();
		if (anteriorEstado != null && anteriorEstado.getTipo().compareTo(ProcesoEstadoDTO.TIPO_ITERADOR) == 0) {
			documentRecentCreateInTransition = iterateInState(respuesta, expedienteDTO, documentoDTO, token, relacionAnterior);
		} else {
			String ubicacion = obtenerUbicacion(documentoDTO, dto.getLlaveTabla(), token);
			System.out.format("\n[%s] Afectando saldos con parametro de la transicion %s", expedienteDTO.getNombre(),
					dto.getAfectaSaldo());
			afectado = moveBalanceDocument(expediente, token, dto, valorModificador, dineroProcesado);
			modificadorId = documentoDTO.getLlaveTabla();
			// Genero documento en caso que toque
			if (dto.getPlantilla() != null) {
				// Tengo que optimizar esto siempre va a preguntar si tiene documentos para
				// generar
				PedidoVentaDTO automatico = createDocumentSinceProperties.generateDocuments(dto, documentoDTO,
						expedienteDTO, documentoDTO.getTransaccion(), token, 0);
				// Por si es la transicion inicial no le quite el poder del documento que genero
				if (automatico != null && automatico.getPlantilla().compareTo(dto.getPlantilla()) == 0)
					modificadorId = automatico.getLlaveTabla();
			}
			System.out.format("\n[%s] Envia a motor de traza por modificador ( %s ) ", expedienteDTO.getNombre(),
					documentoDTO.getNombre());
			// Creo la relacion del documento Gestor
			relacionAnterior = relacionGestorService.trazar(expedienteDTO.getLlaveTabla(), modificadorId,
					nameTrace, dto.getEstadoPartida(), dto.getEstadoLLegada(),
					(afectado == null) ? null : afectado.getLlaveTabla(), ubicacion, token, relacionAnterior,
					expedienteDTO.getHistorico(), transaccion);
		}
		// Se actualiza pedido
		// si son los mismo creo que no necesito update ???????????
		System.out.format("\n[%s] Se actualiza estado del documento de ( %s ) a ( %s )", expedienteDTO.getNombre(),
				expedienteDTO.getEstadoNombre(), filtroEstado.getNombre());
		expedienteDTO.setEstadoExpediente(filtroEstado.getLlaveTabla());
		expedienteDTO.setEstadoNombre(filtroEstado.getNombre());
		// No se porque tenia esta linea ->//anterior.setEstadoNombre(filtroEstado.getNombre());
		expedienteDTO.setEstado(filtroEstado.getEstadoDocumento());
		switch (dto.getEstadoLlegadaTipo()) {
		case ProcesoEstadoDTO.TIPO_DECISION:
			respuesta = resolveStateDesition(dto.getEstadoLLegada(), expediente, documentoDTO.getLlaveTabla(), token);
			UsuarioSesionDTO tokenSystem = autenticacionService.generateAdministratorToken();
			respuesta = executeInternal(respuesta, expediente, documentoDTO, valorModificador, afectado, relacionAnterior,
					tokenSystem.getLlaveTabla(), transaccion, nameTrace, userID, documentRecentCreateInTransition);
			break;
		case ProcesoEstadoDTO.TIPO_ITERADOR:
			respuesta = getNextTransition(dto.getEstadoLLegada(), null);
			// Por si siguen decisiones
			respuesta = executeInternal(respuesta, expediente, documentoDTO, valorModificador, afectado, relacionAnterior,
					token, transaccion, nameTrace, userID, documentRecentCreateInTransition);
			// Aqui tambien gestiona mensajes se duplica porque no evalue bien que eimpato
			// tiene ponerlo antes o despues
			generateMessageService.call(expedienteDTO, dto, null, documentoDTO, token);
			break;
		case ProcesoEstadoDTO.TIPO_API:
			respuesta = executeAPI(dto.getEstadoLLegada(), expedienteDTO, documentoDTO, token, documentRecentCreateInTransition);
			// Por si siguen decisiones
			respuesta = executeInternal(respuesta, expediente, documentoDTO, valorModificador, afectado, relacionAnterior,
					token, transaccion,(previousStep==null)?dto.getEstadoLlegadaNombre() : previousStep + "->" +dto.getEstadoLlegadaNombre(), userID, documentRecentCreateInTransition);
			break;
		default:
			// No entiendo el motivo pero este update se tiene que dejar aqui
			// en logimax hay una transcicion que cambia la cantidad y despues itera si no
			// dejo aqui este update el estado no quedaba correcto quedaba en la iteracion
			pedidoService.update(expedienteDTO);
			UsuarioDTO responsable = assignResponsibleToActivity(expediente, filtroEstado.getLlaveTabla(),
					filtroEstado.getNombre(), documentoDTO.getLlaveTabla(), token);
			generateMessageService.call(expedienteDTO, dto, responsable, documentoDTO, token);
			break;
		}

		return respuesta;
	}

	private List<PedidoVentaDTO> iterateInState(ProcesoTransicionDTO transicionIteracion, // Estado que contine la iteracion y donde
																			// vamos a buscar al funcion
			PedidoVentaDTO expediente, // Documento Proceso que estamos afectando
			PedidoVentaDTO documentoModificador, // Documento que realizo la acción y disparo la transicion
			String token, // Codigo de seguridad de la transaccion
			DocumentoRelacionGestorDTO relacionAnterior // SE necesita para la traza :(
	) throws ServerException {

		ProcesoEstadoDTO pEstadoDTO = estadoService.consultaXId(transicionIteracion.getEstadoPartida());
		if (pEstadoDTO.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO) != 0)
			throw new ServerException("La iteracion " + pEstadoDTO.getNombre() + " esta inactiva");
		PropiedadDTO propiedadFuncion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO,
				pEstadoDTO.getLlaveTabla(), Propiedades.ITERACION_SQL, null);
		if (propiedadFuncion == null)
			throw new ServerException("La iteracion " + pEstadoDTO.getNombre() + " no tiene definida la funcion SQL");

		List<PedidoVentaDTO> documentsToCreate = null;
		try {
			documentsToCreate = pedidoService.iteracionesProceso(SoftureUtil.formatFunction(propiedadFuncion.getLlaveTabla()),
					expediente.getLlaveTabla(),
					(documentoModificador == null) ? null : documentoModificador.getLlaveTabla());
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Iteracion : " + pEstadoDTO.getNombre());
		}
		List<PedidoVentaDTO> result = null;
		if (documentsToCreate != null && !documentsToCreate.isEmpty()) {
			result = new ArrayList<>();
			for (int i = 0; i < documentsToCreate.size(); i++) {
				PedidoVentaDTO iDocumentoIterar = documentsToCreate.get(i);
				iDocumentoIterar.setCaracteristicas(pedidoVentaCaracteristicaService
						.listar2Documento(iDocumentoIterar.getLlaveTabla(), iDocumentoIterar.getHistorico()));
				// Aqui al parecer el expediednte principal es el modificador pero no me parece
				// que sea asi, deberia ser el expediente??, o talvez todos
				PedidoVentaDTO acabdoCrear = createDocumentSinceProperties.generateDocuments(transicionIteracion,
						iDocumentoIterar, documentoModificador, iDocumentoIterar.getTransaccion(), token, i + 1);
				// Creo la relacion del documento Gestor
				relacionGestorService.trazar(expediente.getLlaveTabla(),
						(acabdoCrear == null) ? null : acabdoCrear.getLlaveTabla(), transicionIteracion.getNombre(),
						transicionIteracion.getEstadoPartida(), transicionIteracion.getEstadoLLegada(), null, null,
						token, relacionAnterior, expediente.getHistorico(), null);
				if(acabdoCrear!=null) result.add(acabdoCrear);
			}
			if(result.size()==0) throw new ServerException("No se gneraron documentos en la iteracion revisa las propiedades de la transcion para crear los campos");
		}
		return result;
	}

	private ProcesoTransicionDTO executeAPI(String estadoLlegada, PedidoVentaDTO expedienteDTO,
			PedidoVentaDTO documentoDTO, String token,
			List<PedidoVentaDTO> documentRecentCreateInTransition) throws ServerException {
		ProcesoEstadoDTO apiDTO = estadoService.consultaXId(estadoLlegada);
		if (apiDTO.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO) != 0)
			throw new ServerException("El punto del api " + apiDTO.getNombre() + " esta inactivo");
		apiDTO.setPropiedades(propiedadService.obtenerPropiedadesSinEntidad(PropiedadValorDefinidoDTO.ESTADO,
				estadoLlegada, null, getUserId(token)));

		PropiedadDTO propAPI = Propiedades.obtenerParametro(apiDTO, Propiedades.API);
		if (propAPI == null)
			throw new ServerException(String.format("El estado %s no tiene definido el API", apiDTO.getNombre()));
		
		String resultAPI = ConstantesGenerales.OK;
		if(documentRecentCreateInTransition==null || documentRecentCreateInTransition.isEmpty()) {
			resultAPI = apiService.prepareApiToExecution(propAPI.getValor(), expedienteDTO, documentoDTO, token, null);
		}else {
			// Para el manifiesto primero se crea muchas remesas y despues un solo manifiesto
			PropiedadDTO propOneExecution = Propiedades.obtenerParametro(apiDTO, Propiedades.API_ITERATION_ONE_EXECUTION);
			if (propOneExecution!= null) {
				String stringToDocumentsToAPI = "";
				for (int i = 0; i < documentRecentCreateInTransition.size(); i++) {
					stringToDocumentsToAPI = stringToDocumentsToAPI + ConstantesGenerales.PUNTO_COMA_DOBLE + "ITERADOR_CODE["+ i + "]" + ConstantesGenerales.IGUAL + documentRecentCreateInTransition.get(i).getNombre();
				}
				resultAPI = apiService.prepareApiToExecution(propAPI.getValor(), expedienteDTO, documentoDTO, token, stringToDocumentsToAPI);
			}else {
				// en caso de error solo ejecuto en la proxima trnsaccion los que fueron exitosos
				List<PedidoVentaDTO> okDocumentsInAPI = new ArrayList<>();
				for (PedidoVentaDTO pedidoVentaDTO : documentRecentCreateInTransition) {
					resultAPI = apiService.prepareApiToExecution(propAPI.getValor(), expedienteDTO, pedidoVentaDTO, token, null);
					if(resultAPI.compareTo(ConstantesGenerales.OK)!=0) {
						documentRecentCreateInTransition = okDocumentsInAPI;
						if(!okDocumentsInAPI.isEmpty()) resultAPI = ConstantesGenerales.INCOMPLETE;
						break;
					}
					okDocumentsInAPI.add(pedidoVentaDTO);
				}
			}
			
		}
		return getNextTransition(estadoLlegada, resultAPI);
	}

	private ProcesoTransicionDTO resolveStateDesition(String decision, String llaveTablaDocumento,
			String llaveModificador, String token) throws ServerException {
		ProcesoEstadoDTO decisionDTO = estadoService.consultaXId(decision);
		if (decisionDTO.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO) != 0)
			throw new ServerException("La decision " + decisionDTO.getNombre() + " esta inactiva");
		PropiedadDTO propiedadFuncion = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO, decision,
				Propiedades.DECISION_SQL, getUserId(token));
		if (propiedadFuncion == null)
			throw new ServerException("La decision " + decisionDTO.getNombre() + " no tiene definida la funcion SQL");
		String resultado = null;
		try {
			resultado = procesoTransicionMapper.decision(SoftureUtil.formatFunction(propiedadFuncion.getLlaveTabla()),
					llaveTablaDocumento, llaveModificador);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Decision : " + decisionDTO.getNombre());
		}
		if (resultado == null)
			throw new ServerException("El resultado ha sido nulo\nDecision : " + decisionDTO.getNombre());
		ProcesoTransicionDTO solucion = getNextTransition(decisionDTO.getLlaveTabla(), resultado);
		return solucion;
	}

	private ProcesoTransicionDTO getNextTransition(String estadoActual, String nombreTransicion)
			throws ServerException {
		ProcesoTransicionFilterDTO solucionFilter = new ProcesoTransicionFilterDTO();
		solucionFilter.setEstadoPartida(estadoActual);
		solucionFilter.setNombre(nombreTransicion);
		solucionFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		ProcesoTransicionDTO solucion = transicionService.consultaUnica(solucionFilter);
		if (solucion == null) {
			//La idea es evitar que se pierda informacion enn las apis ya que no se guarda los archivos
			solucionFilter.setNombre(ConstantesGenerales.OK);
			solucion = transicionService.consultaUnica(solucionFilter);
			if (solucion == null) {
				ProcesoEstadoDTO decisionDTO = estadoService.consultaXId(estadoActual);
				String msgException = "La decision "+decisionDTO.getNombre()+" del proceso " + decisionDTO.getProcesoNombre() 
					+ " esta intentando buscar un camino para la respuesta ( " +nombreTransicion +" ), actualmente ";
				solucionFilter.setNombre(null);
				List<ProcesoTransicionDTO> responseTransition = transicionService.listarConsulta(solucionFilter);
				if(responseTransition!=null && !responseTransition.isEmpty()) {
					msgException+= " se tiene configurado respuesta para : ";
					for (int i = 0; i < responseTransition.size(); i++) {
						msgException+= "\n " + String.valueOf(i+1) + " - " + responseTransition.get(i).getNombre();
					}
					msgException+= " \nrevisa la propiedad SQL de la decision";
				}else {
					msgException+= " no tienes configurada ninguna respuesta";
				}
				throw new ServerException(msgException);
			}
		}
		return solucion;
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
			dinero = dineroService.consultaPorDocumento(expediente, pExpediente.getHistorico());
		}

		if (transicion.getAfectaSaldo() == null)
			return dinero;
		if (dinero == null) {
			throw new ServerException("Revise el documento " + pExpediente.getNombre()
					+ " porque no tiene ningun registro de valores de saldos");
		}
		if (saldoDocumento == null)
			throw new ServerException("Revise porque el documento no tiene saldo");

		BigDecimal factor = BigDecimal.ONE;
		if (transicion.getAfectaSaldo().compareTo(ProcesoTransicionDTO.RESTANDO) == 0)
			factor = factor.negate();

		System.out.format("\n[%s] Afectando saldos con factor %s", dinero.getDocumento(), factor.toString());
		if (transicion.getEstadoPartida() == null) { // Para los documentos iniciales
			if (transicion.getAfectaSaldo().compareTo(ProcesoTransicionDTO.SUMANDO) != 0)
				throw new ServerException("No es logico que inicie in proceso restando");
			dinero.setSaldo(dinero.getSaldo().add(saldoDocumento.multiply(factor)));
			//Controlar saldo se coloca para poder controlar los cambios de valores dle documento en el tiempo con modificar
			dinero.setControlarSaldo(true);
			validateSaldo(transicion, saldoDocumento, dinero, pExpediente);
			dineroService.update(dinero);// Se acaba de crear siempre va a ser tabla productiva
			return dinero;
		}
		dineroService.inactivarConHistorial(dinero, pExpediente.getHistorico());
		PedidoVentaDineroDTO nuevo = new PedidoVentaDineroDTO();
		nuevo.setSaldo(dinero.getSaldo().add(saldoDocumento.multiply(factor)));
		//Controlar saldo se coloca para poder controlar los cambios de valores dle documento en el tiempo con modificar
		nuevo.setControlarSaldo(true);
		nuevo.setDocumento(dinero.getDocumento());
		nuevo.setValorTotal(dinero.getValorTotal());
		validateSaldo(transicion, saldoDocumento, nuevo, pExpediente);
		return dineroService.guardarConHistorial(nuevo, pExpediente.getHistorico());
	}

	private void validateSaldo(ProcesoTransicionDTO transicion, BigDecimal saldoDocumento, PedidoVentaDineroDTO saldosCalculados,
			PedidoVentaDTO pExpediente) throws ServerException {
		//System.out.format(
		//		"\n" + transicion.getNombre() + " (" + pExpediente.getNombre() + " : " + saldosCalculados.getValorTotal()
		//				+ ")" + saldosCalculados.getSaldo() + " - " + saldoDocumento + " = " + saldosCalculados.getSaldo());
		if (saldosCalculados.getSaldo().compareTo(BigDecimal.ZERO) < 0) {
			throw new ServerException(transicion.getNombre() + " (" + documentoService.consultaXId(pExpediente.getPlantilla()).getNombre() + " " + pExpediente.getNombre() + " : Por un total de " + SoftureUtil.formatMoney(saldosCalculados.getValorTotal())
				+ ")\n\n Saldos "+ SoftureUtil.formatMoney(saldosCalculados.getSaldo().add(saldoDocumento)) + " - " + SoftureUtil.formatMoney(saldoDocumento) + " = " + SoftureUtil.formatMoney(saldosCalculados.getSaldo()));
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
				anterior.getHistorico(), documento.getTransaccion());
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
