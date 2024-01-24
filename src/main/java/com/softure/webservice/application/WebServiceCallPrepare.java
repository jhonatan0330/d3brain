package com.softure.webservice.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.services.ProcessTemplate;
import com.softure.java.services.SoftureUtil;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.upload.application.UploadSvc;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceEjecucionDTO;

@Component
public class WebServiceCallPrepare {

	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	private WebServiceEjecucionSvc webServiceEjecucionSvc;
	@Autowired
	private RelacionInternaSvc relacionService;
	@Autowired
	private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired
	private UploadSvc uploadService;
	@Autowired
	private ProcessTemplate templatesService;

	public WebServiceEjecucionDTO call(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador,
			String token, String userId, String initialPameters) throws ServerException {
		WebServiceEjecucionDTO callWS = new WebServiceEjecucionDTO();
		callWS.setServicio(service.getLlaveTabla());
		callWS.setUsuario(userId);
		callWS.setFecha(new Date());
		String parameters = getParameters(service, document, modificador, token);
		if (initialPameters != null) {
			parameters = parameters + initialPameters;
		}
		callWS.setParametros(parameters);
		String parameterHelperToLong = null;
		if (callWS.getParametros() != null && callWS.getParametros().length() > 4000) {
			parameterHelperToLong = callWS.getParametros();
			callWS.setParametros(
					uploadService.uploadFile(parameterHelperToLong.getBytes(), "Parameter.txt", token, "webservice"));
		}
		callWS.setDocumento(document.getLlaveTabla());
		callWS.setTransaccion(document.getTransaccion());
		if (modificador != null) {
			callWS.setModificador(modificador.getLlaveTabla());
			callWS.setTransaccion(modificador.getTransaccion());
		}
		callWS = webServiceEjecucionSvc.save(callWS);
		if (parameterHelperToLong != null)
			callWS.setParametros(parameterHelperToLong);
		return callWS;
	}

	/**
	 * Se encarga de tomar los valores de los documentos y generar los campos que se
	 * necesitan para los reemplazos
	 * 
	 * @param service     Coloca las propiedades del API para generar los campos a
	 *                    reemplazar
	 * @param document    Documento base de la transaccion se encesita nombre, llave
	 *                    y plantilla si no tiene campso los consulta solo
	 * @param modificador Documento que realiza las modificaciones a un expediente
	 *                    principal, puede venir vacio, es obligatorio tener llave y
	 *                    plantilla
	 * @return
	 * @throws ServerException
	 */
	private String getParameters(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador,
			String token) throws ServerException {
		String parameters = "";
		if (service.getPropiedades() != null && !service.getPropiedades().isEmpty()) {
			// Directas
			List<PropiedadDTO> directas = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_DIRECT);
			if (directas != null && !directas.isEmpty() && document != null && document.getPlantilla() != null) {
				for (PropiedadDTO iProp : directas) {
					List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(iProp.getLlaveTabla());
					if (relaciones != null && !relaciones.isEmpty()) {
						List<PedidoVentaCaracteristicaDTO> camposOpcionales = null;
						if (document.getCaracteristicas() == null) {
							PedidoVentaCaracteristicaDTO aux = new PedidoVentaCaracteristicaDTO();
							aux.setValorOpcion(document.getLlaveTabla());
							List<PedidoVentaCaracteristicaDTO> listAux = new ArrayList<PedidoVentaCaracteristicaDTO>();
							listAux.add(aux);
							camposOpcionales = campoService.listar2getApiCode(listAux, relaciones);
						} else {
							camposOpcionales = document.getCaracteristicas();
						}
						for (RelacionInternaDTO iRelacion : relaciones) {
							if (iRelacion.getPlantilla().compareTo(document.getPlantilla()) == 0) {
								PedidoVentaCaracteristicaDTO campo = CallDocumentCommons.obtenerValor(camposOpcionales,
										iRelacion.getCampo());
								if (campo != null && campo.getValorText() != null) {
									if (campo.getCampoDTO() == null)
										campo.setCampoDTO(fieldService.consultaXId(campo.getCampo()));
									String codeReplace = (campo.getTransaccionRegistro() == null)
											? campo.getCampoDTO().getCodigo()
											: campo.getTransaccionRegistro();
									parameters = templatesService.addParameterString(parameters, iRelacion, campo,
											codeReplace, "D", iRelacion.getAuxiliar(),
											Propiedades.obtenerVariosParametro(service,
													Propiedades.API_CODE_REFERENCE_LIST));
								}
							}
						}
					}
				}
			}
			// Especiales
			List<PropiedadDTO> especiales = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_ESPECIAL);
			if (especiales != null && !especiales.isEmpty()) {
				for (PropiedadDTO iProp : especiales) {
					if (iProp.getTexto() == null)
						throw new ServerException(
								"Es necesario colocar texto en la propiedad de codigo especial " + iProp.getValor());
					if (iProp.getTexto().startsWith("E_FECHA_")) {
						Date fieldDate = templatesService.getDateWithTransformations(new Date(), iProp.getTexto());
						parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto()
								+ SharedConstants.IGUAL + SoftureUtil.formatDatePattern(fieldDate, iProp.getValor());
					} else {
						switch (iProp.getTexto()) {
						case "E_ID":
							if (document != null)
								parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto()
										+ SharedConstants.IGUAL + document.getLlaveTabla();
							break;
						case "E_CODE":
							if (document != null)
								parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto()
										+ SharedConstants.IGUAL + document.getNombre();
							break;
						case "E_CODE_MODIFICATOR":
							if (modificador != null)
								parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto()
										+ SharedConstants.IGUAL + modificador.getNombre();
							break;
						case "E_TOKEN":
							if (token != null)
								parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto()
										+ SharedConstants.IGUAL + token;
							break;
						default:
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto()
									+ SharedConstants.IGUAL + iProp.getValor();
							break;
						}

					}
				}
			}
			// Referidas
			List<PropiedadDTO> referidas = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_REFERENCE);
			if (referidas != null && !referidas.isEmpty()) {
				for (PropiedadDTO iProp : referidas) {
					parameters = templatesService.extractParameterTypeR(
							Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_REFERENCE_LIST),
							document, modificador, parameters, iProp);
				}
			}
			if (modificador != null) {
				// modificador
				List<PropiedadDTO> modificadoras = Propiedades.obtenerVariosParametro(service,
						Propiedades.API_CODE_MODIFICADOR);
				if (modificadoras != null && !modificadoras.isEmpty()) {
					for (PropiedadDTO iProp : modificadoras) {
						List<RelacionInternaDTO> rModificadoras = relacionService
								.relacionesPropiedad(iProp.getLlaveTabla());
						if (rModificadoras != null && !rModificadoras.isEmpty()) {
							List<PedidoVentaCaracteristicaDTO> camposOpcionales = null;
							if (modificador.getCaracteristicas() == null) {
								PedidoVentaCaracteristicaDTO aux = new PedidoVentaCaracteristicaDTO();
								aux.setValorOpcion(modificador.getLlaveTabla());
								List<PedidoVentaCaracteristicaDTO> listAux = new ArrayList<PedidoVentaCaracteristicaDTO>();
								listAux.add(aux);
								camposOpcionales = campoService.listar2getApiCode(listAux, rModificadoras);
							} else {
								camposOpcionales = modificador.getCaracteristicas();
							}
							for (RelacionInternaDTO iRelacion : rModificadoras) {
								if (iRelacion.getPlantilla().compareTo(modificador.getPlantilla()) == 0) {
									PedidoVentaCaracteristicaDTO campo = CallDocumentCommons
											.obtenerValor(camposOpcionales, iRelacion.getCampo());
									if (campo != null && campo.getValorText() != null) {
										if (campo.getCampoDTO() == null)
											campo.setCampoDTO(fieldService.consultaXId(campo.getCampo()));
										parameters = templatesService.addParameterString(parameters, iRelacion, campo,
												campo.getCampoDTO().getCodigo(), "M", iRelacion.getAuxiliar(),
												Propiedades.obtenerVariosParametro(service,
														Propiedades.API_CODE_REFERENCE_LIST));
									}
								}
							}
						}
					}
				}
			}
		}
		if (parameters == "")
			parameters = null;
		return parameters;
	}

}
