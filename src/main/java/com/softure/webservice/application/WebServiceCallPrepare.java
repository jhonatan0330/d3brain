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
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
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
	@Autowired
	private WebServiceSvc webServiceSvc;
	@Autowired
	private PropiedadSvc propiedadesSvc;

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
		if (service.getPropiedades() == null || service.getPropiedades().isEmpty())
			return null;
		String parameters = "";
		parameters = getDirectParameters(service, document, parameters);
		parameters = getSpecialParameter(service, document, modificador, token, parameters);
		parameters = getReferedParameters(service, document, modificador, parameters);
		parameters = getBaseParameters(service, document, modificador, parameters);
		if (parameters == "")
			parameters = null;
		return parameters;
	}

	// Falta evitar un ciclo infinito puede ser una funcion global que guarde string y que no se repitan esto esta en tipoproceso
	private String getBaseParameters(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador,
			String parameters) throws ServerException {
		List<PropiedadDTO> properties = Propiedades.obtenerVariosParametro(service, Propiedades.API_BASE);
		if (properties == null || properties.isEmpty())return parameters; 
		for (PropiedadDTO iProp : properties) {
			//esta parte se puede centralizar para evitar referencias circulares
			WebServiceDTO baseService = webServiceSvc.consultaXId(iProp.getValor());
			if (baseService == null)
				throw new ServerException("El id del servicio no se encuentra en la BD." + iProp.getValor());
			if (baseService.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException("El servicio " + service.getNombre() + " no se encuentra Activo." + iProp.getValor());
			// Obtengo propiedades del servicio
			baseService.setPropiedades(
					propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE, iProp.getValor(), null, null));
			String baseParameter =  getParameters(baseService, document, modificador, parameters);
			if(baseParameter!=null) {
				if(parameters!=null & !parameters.isEmpty()) parameters =  parameters + SharedConstants.PUNTO_COMA_DOBLE;
				parameters = parameters + baseParameter;
			}
		}
		return parameters;
	}

	private String getReferedParameters(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador,
			String parameters) throws ServerException {
		// Referidas
		List<PropiedadDTO> referidas = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_REFERENCE);
		if (referidas != null && !referidas.isEmpty()) {
			for (PropiedadDTO iProp : referidas) {
				parameters = templatesService.extractParameterTypeR(
						Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_REFERENCE_LIST), document,
						modificador, parameters, iProp);
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
								PedidoVentaCaracteristicaDTO campo = CallDocumentCommons.obtenerValor(camposOpcionales,
										iRelacion.getCampo());
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
		return parameters;
	}

	private String getSpecialParameter(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador,
			String token, String parameters) throws ServerException {
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
		return parameters;
	}

	private String getDirectParameters(WebServiceDTO service, PedidoVentaDTO document, String parameters)
			throws ServerException {
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
										codeReplace, "D", iRelacion.getAuxiliar(), Propiedades
												.obtenerVariosParametro(service, Propiedades.API_CODE_REFERENCE_LIST));
							}
						}
					}
				}
			}
		}
		return parameters;
	}

}
