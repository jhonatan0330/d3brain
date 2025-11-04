package com.softure.webservice.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
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
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.upload.application.UploadSvc;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceEjecucionDTO;

@Component
public class WebServiceCallPrepare {

	@Autowired @Lazy 
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired @Lazy 
	private WebServiceEjecucionSvc webServiceEjecucionSvc;
	@Autowired @Lazy 
	private RelacionInternaSvc relacionService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired @Lazy 
	private UploadSvc uploadService;
	@Autowired @Lazy 
	private ProcessTemplate templatesService;
	@Autowired @Lazy 
	private WebServiceSvc webServiceSvc;
	@Autowired @Lazy 
	private PropiedadSvc propiedadesSvc;

	public WebServiceEjecucionDTO call(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador, PedidoVentaDTO iterador,
			String token, String initialPameters) throws ServerException {
		WebServiceEjecucionDTO callWS = new WebServiceEjecucionDTO();
		callWS.setServicio(service.getLlaveTabla());
		String userId = webServiceSvc.getUserFlex(token);
		callWS.setUsuario(userId);
		callWS.setFecha(new Date());
		//aqui ya viene con todoas las properties
		String parameters = getParameters(service, document, modificador, iterador,  token);
		if (initialPameters != null) {
			parameters = parameters + initialPameters;
		}
		callWS.setParametros(parameters);
		if (callWS.getParametros() != null && callWS.getParametros().length() > 4000) {
			callWS.setParametros(
					uploadService.uploadFile(callWS.getParametros().getBytes(), "Parameter.txt", token, "webservice", "private"));
		}
		callWS.setDocumento(document.getLlaveTabla());
		callWS.setTransaccion(document.getTransaccion());
		if (modificador != null) {
			callWS.setModificador(modificador.getLlaveTabla());
			callWS.setTransaccion(modificador.getTransaccion());
		}
		callWS = webServiceEjecucionSvc.save(callWS);
		callWS.setParametersInexecution(parameters);
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
	private String getParameters(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador, PedidoVentaDTO iterador,
			String token) throws ServerException {
		if (service.getPropiedades() == null || service.getPropiedades().isEmpty())
			return null;
		String parameters = "";
		parameters = getDirectParameters(service, document, parameters);
		parameters = getSpecialParameter(service, document, modificador, iterador, token, parameters);
		parameters = getReferedParameters(service, document, modificador, parameters, iterador);
		if (parameters == "") 			
			return null;
        String[] partes = parameters.split(";;");
        // 2. Usar HashSet para quitar duplicados
        Set<String> sinDuplicados = new HashSet<>();
        for (String parte : partes) {
            sinDuplicados.add(parte.trim());
        }
        // 3. Convertir a lista y ordenar
        List<String> ordenadas = new ArrayList<>(sinDuplicados);
        Collections.sort(ordenadas);
        // 4. Unir nuevamente con ";;"
        return  String.join(";;", ordenadas);
	}	

	private String getReferedParameters(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador,
			String parameters, PedidoVentaDTO iterador) throws ServerException {
		// Referidas
		List<PropiedadDTO> referidas = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_REFERENCE);
		if (referidas != null && !referidas.isEmpty()) {
			for (PropiedadDTO iProp : referidas) {
				parameters = templatesService.extractParameterTypeR(
						Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_REFERENCE_LIST), document,
						modificador, parameters, iProp, iterador);
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
													Propiedades.API_CODE_REFERENCE_LIST), SharedConstants.PUNTO_COMA_DOBLE, SharedConstants.IGUAL);
								}
							}
						}
					}
				}
			}
		}
		return parameters;
	}

	private String getSpecialParameter(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador, PedidoVentaDTO iterator,
			String token, String parameters) throws ServerException {
		// Especiales
		List<PropiedadDTO> especiales = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_ESPECIAL);
		if (especiales != null && !especiales.isEmpty()) {
			for (PropiedadDTO iProp : especiales) {
				if (iProp.getTexto() == null)iProp.setTexto(iProp.getValor());
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
									+ SharedConstants.IGUAL + document.getNombre() + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto() + "_ID"
									+ SharedConstants.IGUAL + document.getLlaveTabla();
						if (modificador != null)
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE +  "E_CODE_MODIFICATOR"
									+ SharedConstants.IGUAL + modificador.getNombre()+ SharedConstants.PUNTO_COMA_DOBLE + "E_ID_MODIFICATOR"
									+ SharedConstants.IGUAL + modificador.getLlaveTabla();
						if (iterator != null)
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE +  "E_CODE_ITERATOR"
									+ SharedConstants.IGUAL + iterator.getNombre()+ SharedConstants.PUNTO_COMA_DOBLE + "E_ID_ITERADOR"
									+ SharedConstants.IGUAL + iterator.getLlaveTabla();
						break;
					case "E_CODE_MODIFICATOR":
						if (modificador != null)
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto()
									+ SharedConstants.IGUAL + modificador.getNombre()+ SharedConstants.PUNTO_COMA_DOBLE + "E_ID_MODIFICATOR"
									+ SharedConstants.IGUAL + modificador.getLlaveTabla();
						if (iterator != null)
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "E_CODE_ITERATOR"
									+ SharedConstants.IGUAL + iterator.getNombre()+ SharedConstants.PUNTO_COMA_DOBLE + "E_ID_ITERADOR"
									+ SharedConstants.IGUAL + iterator.getLlaveTabla();
						if (document != null)
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "E_CODE"
									+ SharedConstants.IGUAL + document.getNombre() + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto() + "_ID"
									+ SharedConstants.IGUAL + document.getLlaveTabla();
						break;
					case "E_TOKEN":
						if (token != null)
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto()
									+ SharedConstants.IGUAL + token;
						break;
					case "E_ALL":
						if (modificador != null)
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "E_CODE_MODIFICATOR"
									+ SharedConstants.IGUAL + modificador.getNombre()+ SharedConstants.PUNTO_COMA_DOBLE + "E_ID_MODIFICATOR"
									+ SharedConstants.IGUAL + modificador.getLlaveTabla();
						if (iterator != null)
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "E_CODE_ITERATOR"
									+ SharedConstants.IGUAL + iterator.getNombre()+ SharedConstants.PUNTO_COMA_DOBLE + "E_ID_ITERADOR"
									+ SharedConstants.IGUAL + iterator.getLlaveTabla();
						if (document != null) {
							parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "E_CODE"
									+ SharedConstants.IGUAL + document.getNombre() + SharedConstants.PUNTO_COMA_DOBLE + "E_CODE_ID"
									+ SharedConstants.IGUAL + document.getLlaveTabla();
							if(document.getFecha()!=null)
								parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "E_CODE_FECHA"
										+ SharedConstants.IGUAL +  SoftureUtil.formatDatePattern(document.getFecha(), "LOCAL_API");
						}
							
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
												.obtenerVariosParametro(service, Propiedades.API_CODE_REFERENCE_LIST), SharedConstants.PUNTO_COMA_DOBLE, SharedConstants.IGUAL);
							}
						}
					}
				}
			}
		}
		return parameters;
	}

}
