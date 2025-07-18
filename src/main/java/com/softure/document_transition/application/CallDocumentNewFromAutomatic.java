package com.softure.document_transition.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.softure.authentication.application.UsuarioSesionSvc;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaDineroSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;

@Component
public class CallDocumentNewFromAutomatic {

	@Autowired
	@Lazy
	private RelacionInternaSvc relacionService;
	@Autowired
	@Lazy
	private DocumentoPlantillaSvc plantillaService;
	@Autowired
	@Lazy
	private PropiedadSvc propiedadService;
	@Autowired
	@Lazy
	private CallDocumentCRUD saveUpdateInactivateDocumentFunction;
	@Autowired
	@Lazy
	private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired
	@Lazy
	private UsuarioSesionSvc autenticacionService;
	@Autowired
	@Lazy
	private DocumentoPlantillaCaracteristicaSvc fieldsOfTemplateService;
	@Autowired
	@Lazy
	private PedidoVentaDineroSvc dineroService;

	public PedidoVentaDTO generateDocumentsFromAutomaticTask(ProcesoTransicionDTO transicion, PedidoVentaDTO expedienteDTO, String transaccion, String token,
			PedidoVentaCaracteristicaDTO vieneAutomatica) throws ServerException {
		List<PedidoVentaCaracteristicaDTO> camposNuevos = new ArrayList<PedidoVentaCaracteristicaDTO>();
		camposNuevos.add(vieneAutomatica);
		return processNewFields(transicion, transaccion, token, camposNuevos, null);
	}

	/**
	 * 
	 * @param transicion
	 * @param pGenerator       Documento modificador que realiza la accion sobre el
	 *                        documetnto base
	 * @param expedienteDTO   Documento base que se esta fafectando con el proceso
	 * @param transaccion     Como reuso esto en los temporizadores automaticos
	 *                        entonces no viene transaccion
	 * @param token
	 * @param vieneAutomatica Machetazo toca dividir 2 funcione
	 * @return
	 * @throws ServerException
	 */
	public PedidoVentaDTO generateDocuments(ProcesoTransicionDTO transicion, PedidoVentaDTO pGenerator,
			PedidoVentaDTO expedienteDTO, String transaccion, String token, int iterationNumber,
			Map<String, List<PedidoVentaDTO>> stackDocumentsCreateInTransaction, PedidoVentaDTO pDocumentIterate) throws ServerException {
		List<PedidoVentaCaracteristicaDTO> camposNuevos = new ArrayList<PedidoVentaCaracteristicaDTO>();
		// Por aqui voy cuando viene en una iteracion pero no se que otros caso pueda
		// intente refactor pero salio un aviso asi que deje quieto mientras
		if (transicion.getPlantilla() == null)
			return null;
		String user = getUserId(token);
		transicion.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION,
				transicion.getLlaveTabla(), null, user));
		String[] cars = { Propiedades.GENERA_DOCUMENTO_CAMPO, Propiedades.GENERA_DOCUMENTO_TEXTO,
				Propiedades.GENERA_DOCUMENTO_FUNCION_SQL, Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE,
				Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR,
				Propiedades.GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION };
		List<PropiedadDTO> camposGenerar = Propiedades.obtenerVariosParametro(transicion, cars);

		if ((camposGenerar == null || camposGenerar.isEmpty())
				&& (stackDocumentsCreateInTransaction == null || stackDocumentsCreateInTransaction.isEmpty()))
			return null;

		if (camposGenerar != null) {

			for (PropiedadDTO iPropiedadDTO : camposGenerar) {
				switch (iPropiedadDTO.getKey()) {
				case Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR:
					if (pGenerator != null) {
						PedidoVentaCaracteristicaDTO campoPrincipal = CallDocumentCommons.copyFieldDocument(null, iPropiedadDTO.getValor());
						campoPrincipal.setValorOpcion(pGenerator.getLlaveTabla());
						if (pGenerator.getDinero() != null)
							// Importante para que coja valor porque va a consultar po BD y no tiene
							campoPrincipal.setValorNumero(pGenerator.getDinero().getValorTotal());
						campoPrincipal.setPrincipal(pGenerator);
						camposNuevos.add(campoPrincipal);
					}
					break;
				case Propiedades.GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE:
					if (expedienteDTO != null) {
						PedidoVentaCaracteristicaDTO campoPrincipal = CallDocumentCommons.copyFieldDocument(null, iPropiedadDTO.getValor());
						campoPrincipal.setValorOpcion(expedienteDTO.getLlaveTabla());
						if (expedienteDTO.getDinero() != null)
							campoPrincipal.setValorNumero(expedienteDTO.getDinero().getValorTotal());
						campoPrincipal.setPrincipal(expedienteDTO);
						camposNuevos.add(campoPrincipal);
						break;
					}
				case Propiedades.GENERA_DOCUMENTO_CAMPO:
					List<RelacionInternaDTO> relaciones = relacionService
							.relacionesPropiedad(iPropiedadDTO.getLlaveTabla());
					if (relaciones == null || relaciones.isEmpty()) {
						throw new ServerException("La propiedad " + iPropiedadDTO.getNombre() + " de la transicion "
								+ transicion.getNombre() + " del proceso " + transicion.getProcesoNombre()
								+ " con estado inicial " + transicion.getEstadoPartidaNombre()
								+ ", no tiene relaciones, usa las relaciones para identificar que campo deseas copiar");
					}
					for (RelacionInternaDTO iRelacion : relaciones) {
						if (pGenerator != null && pGenerator.getPlantilla() != null && iRelacion.getPlantilla().compareTo(pGenerator.getPlantilla()) == 0) {
							if (pGenerator.getCaracteristicas() == null)
								pGenerator.setCaracteristicas(pedidoVentaCaracteristicaService.listar2Documento(
										pGenerator.getLlaveTabla(), pGenerator.getHistorico()));
							camposNuevos.add(CallDocumentCommons.copyFieldDocument(CallDocumentCommons.obtenerValor(pGenerator.getCaracteristicas(), iRelacion.getCampo()), iPropiedadDTO.getValor()));
							break;
						}
						if (expedienteDTO != null && expedienteDTO.getPlantilla() != null
								&& iRelacion.getPlantilla().compareTo(expedienteDTO.getPlantilla()) == 0) {
							// Solo consulto el documento cuando en realidad lo necesito, en general no veien las caracteristicas
							if (expedienteDTO.getCaracteristicas() == null)
								expedienteDTO.setCaracteristicas(pedidoVentaCaracteristicaService.listar2Documento(expedienteDTO.getLlaveTabla(), expedienteDTO.getHistorico()));
							camposNuevos.add(CallDocumentCommons.copyFieldDocument(CallDocumentCommons.obtenerValor(expedienteDTO.getCaracteristicas(), iRelacion.getCampo()),iPropiedadDTO.getValor()));
							break;
						}
						if (pDocumentIterate != null && pDocumentIterate.getPlantilla() != null
								&& iRelacion.getPlantilla().compareTo(pDocumentIterate.getPlantilla()) == 0) {
							if (pDocumentIterate.getCaracteristicas() == null)
								pDocumentIterate.setCaracteristicas(pedidoVentaCaracteristicaService.listar2Documento(pDocumentIterate.getLlaveTabla(), pDocumentIterate.getHistorico()));
							camposNuevos.add(CallDocumentCommons.copyFieldDocument(CallDocumentCommons.obtenerValor(pDocumentIterate.getCaracteristicas(), iRelacion.getCampo()),iPropiedadDTO.getValor()));
							break;
						}
					}
					break;
				case Propiedades.GENERA_DOCUMENTO_FUNCION_SQL:
					PedidoVentaCaracteristicaDTO campoGenerado = null;
					try {
						campoGenerado = pedidoVentaCaracteristicaService.consultarSQLCampoGenerarDocumento(
								iPropiedadDTO.getLlaveTabla(),
								(expedienteDTO != null) ? expedienteDTO.getLlaveTabla() : null,
								(pGenerator != null) ? pGenerator.getLlaveTabla() : null);
					} catch (Exception ex) {
						throw new ServerException("Se presento un error en la consulta de la transicion "
								+ transicion.getNombre() + " del proceso " + transicion.getProcesoNombre(),
								ex.getMessage());
					}
					if (campoGenerado != null) {
						List<RelacionInternaDTO> relations = relacionService
								.relacionesPropiedad(iPropiedadDTO.getLlaveTabla());
						if (relations == null || relations.isEmpty())
							throw new ServerException("La propiedad " + iPropiedadDTO.getNombre()
									+ "No tiene relaciones, usa las relaciones para identificar que campo deseas copiar");
						for (RelacionInternaDTO iRelacion : relations) {
							if (pGenerator != null
									&& iRelacion.getPlantilla().compareTo(transicion.getPlantilla()) == 0) {
								camposNuevos.add(CallDocumentCommons.copyFieldDocument(campoGenerado, iRelacion.getCampo()));
								break;
							}
						}
					}
					break;
				case Propiedades.GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION:
					if (pDocumentIterate != null) {
						PedidoVentaCaracteristicaDTO fieldNewFromIteration = CallDocumentCommons.copyFieldDocument(null,
								iPropiedadDTO.getValor());
						fieldNewFromIteration.setValorOpcion(pDocumentIterate.getLlaveTabla());
						fieldNewFromIteration.setValorText(pDocumentIterate.getNombre());
						fieldNewFromIteration.setValorFecha(pDocumentIterate.getFecha());
						fieldNewFromIteration.setValorNumero(pDocumentIterate.getConsecutivo());
						camposNuevos.add(fieldNewFromIteration);	
					}
					break;
				case Propiedades.GENERA_DOCUMENTO_TEXTO:
					String textValueToNewField = iPropiedadDTO.getValor();

					List<RelacionInternaDTO> relacionesNumber = relacionService
							.relacionesPropiedad(iPropiedadDTO.getLlaveTabla());
					if (relacionesNumber == null || relacionesNumber.isEmpty())
						throw new ServerException("La propiedad " + iPropiedadDTO.getNombre()
								+ "No tiene relaciones, usa las relaciones para identificar que campo deseas que contenga el consecutivo");
					PedidoVentaCaracteristicaDTO fieldNew = CallDocumentCommons.copyFieldDocument(null, relacionesNumber.get(0).getCampo());
					if (textValueToNewField.compareTo("#NUMBER") == 0) {
						fieldNew.setValorNumero(new BigDecimal(iterationNumber));
						fieldNew.setValorText(String.valueOf(iterationNumber));
					} else {
						fieldNew.setValorText(textValueToNewField);
					}
					camposNuevos.add(fieldNew);
					break;
				default:
					break;
				}
			}
		}
		if (stackDocumentsCreateInTransaction != null && stackDocumentsCreateInTransaction.size() > 0) {
			List<DocumentoPlantillaCaracteristicaDTO> _fieldsOfTemplate = this.fieldsOfTemplateService
					.listarCamposPlantilla(transicion.getPlantilla(), token);
			for (Map.Entry<String, List<PedidoVentaDTO>> _entry : stackDocumentsCreateInTransaction.entrySet()) {
				for (DocumentoPlantillaCaracteristicaDTO _iCampo : _fieldsOfTemplate) {
					if (_entry.getKey().compareTo(_iCampo.getLlaveTabla()) == 0) {
						PedidoVentaCaracteristicaDTO _fieldNew = CallDocumentCommons.copyFieldDocument(null, _iCampo.getLlaveTabla());
						_fieldNew.setExpedientes(_entry.getValue());
						// Como de las iteraciones no traigo valores, es necesario buscar el valor y asi
						// no me saca error
						if (_fieldNew.getExpedientes() != null && !_fieldNew.getExpedientes().isEmpty()) {
							for (PedidoVentaDTO _expediente : _fieldNew.getExpedientes()) {
								if (_expediente.getDinero() == null)
									_expediente
											.setDinero(dineroService.consultaPorDocumento(_expediente.getLlaveTabla(),
													_expediente.getHistorico(), _expediente.getNombre()));
							}
						}

						camposNuevos.add(_fieldNew);
						break;
					}
				}
			}
		}

		return processNewFields(transicion, transaccion, token, camposNuevos, pGenerator);
	}

	private PedidoVentaDTO processNewFields(ProcesoTransicionDTO transicion,
			String transaccion, String token, List<PedidoVentaCaracteristicaDTO> camposNuevos, PedidoVentaDTO pGenerator) throws ServerException {
		if (!camposNuevos.isEmpty()) {
			String userAdmin = autenticacionService.getUserSystemKey();
			if (userAdmin == null)
				throw new ServerException("Es indispensable configurar el usuario administrador");
			DocumentoPlantillaDTO pPlantilla = new DocumentoPlantillaDTO();
			pPlantilla.setLlaveTabla(transicion.getPlantilla());
			pPlantilla = plantillaService.obtenerCampos(pPlantilla, token, false);
			PedidoVentaDTO nuevo = CallDocumentCommons.generateNewDocument(pPlantilla, transaccion, token, camposNuevos, userAdmin);
			return saveUpdateInactivateDocumentFunction.saveWithoutTransaction(nuevo, token, true, pGenerator);
		} else {
			return null;
		}
	}
	

	private String getUserId(String token) throws ServerException {
		return plantillaService.getUserFlex(token);
	}
}
