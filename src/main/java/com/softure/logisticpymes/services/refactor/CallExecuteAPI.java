package com.softure.logisticpymes.services.refactor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.PropiedadDTO;
import com.softure.logisticpymes.domain.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.domain.dto.RelacionInternaDTO;
import com.softure.logisticpymes.domain.dto.WebServiceDTO;
import com.softure.logisticpymes.domain.dto.WebServiceEjecucionDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.services.DocumentoTransaccionSvc;
import com.softure.logisticpymes.services.MensajeSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.services.PropiedadSvc;
import com.softure.logisticpymes.services.RelacionInternaSvc;
import com.softure.logisticpymes.services.UploadSvc;
import com.softure.logisticpymes.services.WebServiceEjecucionSvc;
import com.softure.logisticpymes.services.WebServiceSvc;
import com.softure.logisticpymes.services.adapter.Propiedades;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CallExecuteAPI {

	private static final String ERROR_EXTRAYENDO = "Error extrayendo el siguiente regular pattern (mira la funcion matches de Java String): ";
	@Autowired
	private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired
	private DocumentoPlantillaSvc templateService;
	@Autowired
	private CallDocumentUpdateFromAutomatic documentAutomaticUpdateFunction;
	@Autowired
	private PropiedadSvc propiedadesSvc;
	@Autowired
	private PedidoVentaSvc documentSvc;
	@Autowired
	private RelacionInternaSvc relacionService;
	@Autowired
	private UploadSvc uploadService;
	@Autowired
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	private WebServiceSvc webServiceSvc;
	@Autowired
	private WebServiceEjecucionSvc webServiceEjecucionSvc;
	@Autowired
	private MensajeSvc mensajeSvc;

	/**
	 * Primero crea el objeto de ejecucion y posteriomente ejecuta el api, exite la
	 * propiedad {@link PropiedadValorDefinidoDTO.API_SERVICE} que hace no se
	 * ejecute inmediatamente el api sino hasta finalizar todos los procesos
	 * 
	 * @param serviceId   Id del api a ejecutar
	 * @param document    Expediente ??
	 * @param modificador documento generador
	 * @param token
	 * @return
	 * @throws ServerException
	 */
	public String prepareApiToExecution(String serviceId, PedidoVentaDTO document, PedidoVentaDTO modificador,
			String token, String previousParameter) throws ServerException {
		// Valido existencia del servicio
		WebServiceDTO service = webServiceSvc.consultaXId(serviceId);
		if (service == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + serviceId);
		// Obtengo propiedades del servicio
		String userId = webServiceSvc.getUserFlex(token);
		service.setPropiedades(
				propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE, serviceId, null, userId));
		// Inicia ejecucion
		log.info("[%s] Procesando API (%s)", document.getNombre(), service.getNombre());
		WebServiceEjecucionDTO apiBasic = generateAsyncWebService(service, document, modificador, token, userId, previousParameter);
		String result = ConstantesGenerales.OK;
		// En caso que la ejecucion sea asincrona omito call api
		if (Propiedades.obtenerParametro(service, Propiedades.API_ASYNCHRONOUS) == null) {
			result = executeApi(service, apiBasic, token, modificador);
		} else {
			apiBasic.setSincrona(DocumentoTransaccionSvc.API_ASYNC);
			webServiceEjecucionSvc.update(apiBasic);
			// transaccionSvc.registrarSincronizacion(apiBasic.getTransaccion(),
			// DocumentoTransaccionSvc.API_ASYNC);
		}
		return result;
	}

	/**
	 * 
	 * @param service
	 * @param callWS
	 * @param token
	 * @param modificador Incluyo al modificador para agregarle los campos nuevos
	 * @return
	 * @throws ServerException
	 */
	public String executeApi(WebServiceDTO service, WebServiceEjecucionDTO callWS, String token,
			PedidoVentaDTO modificador) throws ServerException {
		if (callWS.getFechaEjecucion() != null)
			return ConstantesGenerales.OK;
		if (service.getPropiedades() == null) {
			service.setPropiedades(propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE,
					service.getLlaveTabla(), null, null));
		}
		// Realizo la autenticacion
		String result = ConstantesGenerales.OK;
		WebServiceEjecucionDTO authenticationWS = executeAuthenticationWebService(service, callWS, token);
		String tokenAuthentication = null;
		if (authenticationWS != null) {
			if (authenticationWS.getError() != null) {
				if (callWS.getSincrona() != null) {
					callWS.setSincrona(null);
				}
				callWS.setFechaEjecucion(new Date());
				callWS.setError(authenticationWS.getError());
				webServiceEjecucionSvc.update(callWS);
				publishErrorMessage(service, authenticationWS);
				log.info("[%s] Finalizando API (%s) por error de autenticacion", callWS.getDocumento(),
						service.getNombre());
				return ConstantesGenerales.ERROR;
			}
			if (authenticationWS.getExtracciones() != null)
				tokenAuthentication = authenticationWS.getExtracciones();
		}
		Map<String, String> headers = getHeaderProperties(service, tokenAuthentication);
		// Execution
		callWS = launchWebService(service, callWS, token, headers, modificador);
		// Primero intento de nuevo ejecutarlo
		if (callWS.getError() != null)
			callWS = tryAgain(service, callWS, token, 1, headers, modificador);
		// Si despues de todos los intentos no funciona ya se responde error
		if (callWS.getError() != null) {
			result = ConstantesGenerales.ERROR;
			publishErrorMessage(service, callWS);
		} else {
			callWS.setMasivo(generateDocuments(service, callWS.getTextoRespuesta(), token));
			if (callWS.getMasivo() != null && callWS.getMasivo().compareTo("") != 0) {
				webServiceEjecucionSvc.update(callWS);
			}
		}
		log.info("[%s] Finalizando API (%s)", callWS.getDocumento(), service.getNombre());
		return result;
	}

	private void publishErrorMessage(WebServiceDTO service, WebServiceEjecucionDTO callWS) {
		try {
			String infoError = callWS.getError();
			infoError = infoError + "\nDocumento Principal: "
					+ documentSvc.consultaXId(callWS.getDocumento()).getNombre();
			if (callWS.getModificador() != null)
				infoError = infoError + "\nDocumento generador: "
						+ documentSvc.consultaXId(callWS.getModificador()).getNombre();
			infoError = infoError + "\nEntrada " + callWS.getEntrada();
			infoError = infoError + "\nRespuesta " + callWS.getSalida();
			infoError = infoError + "\n\nId " + callWS.getLlaveTabla() + " [" + SoftureUtil.formatDateTime(new Date()) + "]";
			mensajeSvc.mensaje2Administrator("Error en ejecucion de un API " + service.getNombre(), infoError);
		} catch (Exception e) {
			callWS.setError(callWS.getError() + " \n\nError al notificar a administrador:  " + e.getMessage());
		}
	}

	/**
	 * 
	 * @param service
	 * @param token
	 * @param userId
	 * @return
	 * @throws ServerException
	 */
	private WebServiceEjecucionDTO executeAuthenticationWebService(WebServiceDTO service, WebServiceEjecucionDTO callWS,
			String token) throws ServerException {
		PropiedadDTO authenticationProp = Propiedades.obtenerParametro(service, Propiedades.API_AUTHENTICATION);
		if (authenticationProp == null)
			return null;
		WebServiceDTO authenticationEndPoint = webServiceSvc.consultaXId(authenticationProp.getValor());
		if (authenticationEndPoint == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + authenticationProp.getValor());
		authenticationEndPoint.setPropiedades(propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE,
				authenticationEndPoint.getLlaveTabla(), null, callWS.getUsuario()));
		Map<String, String> headers = getHeaderProperties(authenticationEndPoint, null);
		// *****Execute
		PedidoVentaDTO documentMain = new PedidoVentaDTO();
		documentMain.setLlaveTabla(callWS.getDocumento());
		WebServiceEjecucionDTO authenticationWS = generateAsyncWebService(authenticationEndPoint, documentMain, null,
				token, callWS.getUsuario(), null);
		return launchWebService(authenticationEndPoint, authenticationWS, token, headers, null);
	}

	/**
	 * 
	 * @param service
	 * @param document
	 * @param modificador
	 * @param token
	 * @param userId
	 * @return
	 * @throws ServerException
	 */
	private WebServiceEjecucionDTO generateAsyncWebService(WebServiceDTO service, PedidoVentaDTO document,
			PedidoVentaDTO modificador, String token, String userId, String initialPameters)
			throws ServerException {
		WebServiceEjecucionDTO callWS = new WebServiceEjecucionDTO();
		callWS.setServicio(service.getLlaveTabla());
		callWS.setUsuario(userId);
		callWS.setFecha(new Date());
		String parameters = getParameters(service, document, modificador);
		if (initialPameters!=null) { parameters = parameters + initialPameters; }
		callWS.setParametros(parameters);
		callWS.setDocumento(document.getLlaveTabla());
		callWS.setTransaccion(document.getTransaccion());
		if (modificador != null) {
			callWS.setModificador(modificador.getLlaveTabla());
			callWS.setTransaccion(modificador.getTransaccion());
		}
		return webServiceEjecucionSvc.save(callWS);
	}

	/**
	 * 
	 * @param service          Datos del API, el template, el nombre, las
	 *                         propiedades de validacion y extraccion
	 * @param callWS           Api a ejecutar
	 * @param test
	 * @param headerProperties
	 * @param modificador      Incluyo al modificador para agregarle los campos
	 *                         nuevos
	 * @return
	 * @throws ServerException
	 */
	private WebServiceEjecucionDTO launchWebService(WebServiceDTO service, WebServiceEjecucionDTO callWS, String token,
			Map<String, String> headerProperties, PedidoVentaDTO modificador) throws ServerException {

		String template = generateOutputFile(service.getTemplate(), callWS.getParametros());
		String fullOutput = writeHeadersAndUrl(headerProperties, service.getServidorNombre(), callWS.getParametros(),
				callWS.getExtracciones()) + template;
		callWS.setEntrada(uploadService.uploadFile(fullOutput.getBytes(), "Entrada.txt", token));
		String responseApi = null;
		try {
			responseApi = callApi(service, template, headerProperties);
			callWS.setError(validateResultAPI(responseApi,
					Propiedades.obtenerVariosParametro(service, Propiedades.API_VALIDATION)));
			if (callWS.getError() == null) {
				String[] props = { Propiedades.API_EXTRACTION, Propiedades.API_EXTRACTION_TO_BASE_64 };
				List<PropiedadDTO> extractionProperties = Propiedades.obtenerVariosParametro(service, props);
				String resultExtraction = extractionResultAPI(responseApi, extractionProperties, token);
				if (resultExtraction != null) {
					if (resultExtraction.startsWith(ERROR_EXTRAYENDO)) {
						callWS.setError(resultExtraction);
						responseApi = resultExtraction + "\n\n" + responseApi;
					} else {
						callWS.setExtracciones(resultExtraction);
						if (modificador != null)
							documentAutomaticUpdateFunction.executeFromAPIExtraction(modificador, extractionProperties,
									token, resultExtraction);
					}
				}
			} else {
				responseApi = callWS.getError() + "\n\n" + responseApi;
			}
		} catch (Exception e) {
			if (responseApi == null)
				responseApi = "";
			responseApi = e.getMessage() + "\n\n" + responseApi;
			callWS.setError(e.getMessage());
			log.info("[] Procesando API error (%s)", e.getMessage());
		}

		if (callWS.getExtracciones() != null)
			responseApi = "Extracciones\n\n" + callWS.getExtracciones() + "\n\n" + responseApi;
		callWS.setSalida(uploadService.uploadFile(responseApi.getBytes(), "Salida.txt", token));
		callWS.setFechaEjecucion(new Date());
		callWS = webServiceEjecucionSvc.update(callWS);
		callWS.setTextoRespuesta(responseApi);
		return callWS;
	}

	/**
	 * 
	 * @param service
	 * @param callWS
	 * @param token
	 * @param countIteration
	 * @param headers
	 * @return
	 * @throws ServerException
	 */
	private WebServiceEjecucionDTO tryAgain(WebServiceDTO service, WebServiceEjecucionDTO callWS, String token,
			int countIteration, Map<String, String> headers, PedidoVentaDTO modificador) throws ServerException {
		PropiedadDTO tryProp = Propiedades.obtenerParametro(service, Propiedades.API_MAX_TRY);
		if (tryProp == null)
			return callWS;
		try {
			int maxTry = Integer.parseInt(tryProp.getValor());
			if (countIteration < maxTry && countIteration < 3) {
				callWS = launchWebService(service, callWS, token, headers, modificador);
				if (callWS.getError() != null)
					callWS = tryAgain(service, callWS, token, countIteration + 1, headers, modificador);
			}
		} catch (NumberFormatException e) {
		}
		return callWS;
	}

	/**
	 * 
	 * @param response             Respuesta de la ejecucion del api
	 * @param validationProperties servicio para obtener las propiedades de
	 *                             validacion
	 * @return
	 */
	private String validateResultAPI(String response, // Respuesta de la ejecucion del api
			List<PropiedadDTO> validationProperties // propiedades de validacion
	) {
		if (validationProperties == null || validationProperties.isEmpty())
			return null;
		for (PropiedadDTO propiedadDTO : validationProperties) {
			if (!response.matches(propiedadDTO.getValor())) {
				return "Error validando el siguiente regular pattern (mira la funcion matches de Java String): "
						+ propiedadDTO.getValor();
			}
		}
		return null;
	}

	/**
	 * Realiza las extracciones del resultado del API
	 * 
	 * @param responseApi    Contiene la respues del API
	 * @param extractionList
	 * @param callWS
	 * @return
	 * @throws ServerException
	 */
	private String extractionResultAPI(String responseApi, List<PropiedadDTO> extractionList, String token)
			throws ServerException {
		if (extractionList == null || extractionList.isEmpty())
			return null;
		String result = "";
		for (PropiedadDTO propiedadDTO : extractionList) {
			final Matcher matcher = Pattern.compile(propiedadDTO.getValor()).matcher(responseApi);
			if (!matcher.matches()) {
				return ERROR_EXTRAYENDO + propiedadDTO.getValor();
			}
			String newValue = matcher.group(1);
			if (propiedadDTO.getKey().compareTo(Propiedades.API_EXTRACTION_TO_BASE_64) == 0) {
				newValue = uploadService.uploadFile(uploadService.transformBase64ToPDF(newValue),
						Propiedades.API_EXTRACTION_TO_BASE_64 + ".pdf", token);
			}

			String codeAndEqual = ((propiedadDTO.getTexto() == null) ? propiedadDTO.getLlaveTabla()
					: propiedadDTO.getTexto());
			if (!codeAndEqual.contains(ConstantesGenerales.IGUAL))
				codeAndEqual = codeAndEqual + ConstantesGenerales.IGUAL;
			// en la extraccion de autenticacion debo colocar el header
			result = result + ConstantesGenerales.PUNTO_COMA_DOBLE + codeAndEqual + newValue;
		}
		if (result == "")
			result = null;
		return result;
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
	private String getParameters(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador)
			throws ServerException {
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
									parameters = addParameterString(parameters, iRelacion, campo, codeReplace, "D");
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
						Date fieldDate = getDateWithTransformations(iProp.getTexto());
						parameters = parameters + ConstantesGenerales.PUNTO_COMA_DOBLE +  iProp.getTexto()
								+ ConstantesGenerales.IGUAL
								+ SoftureUtil.formatDatePattern(fieldDate, iProp.getValor());
					} else {
						switch (iProp.getTexto()) {
						case "E_ID":
							if (document != null)
								parameters = parameters + ConstantesGenerales.PUNTO_COMA_DOBLE 
										+ iProp.getTexto() + ConstantesGenerales.IGUAL
										+ document.getLlaveTabla();
							break;
						case "E_CODE":
							if (document != null)
								parameters = parameters + ConstantesGenerales.PUNTO_COMA_DOBLE 
										+ iProp.getTexto() + ConstantesGenerales.IGUAL
										+ document.getNombre();
							break;
						case "E_CODE_MODIFICATOR":
							if (modificador != null)
								parameters = parameters + ConstantesGenerales.PUNTO_COMA_DOBLE 
										+ iProp.getTexto()  + ConstantesGenerales.IGUAL
										+ modificador.getNombre();
							break;
						default:
							parameters = parameters + ConstantesGenerales.PUNTO_COMA_DOBLE + iProp.getTexto()
									+ ConstantesGenerales.IGUAL + iProp.getValor();
							break;
						}

					}
				}
			}
			// Referidas
			List<PropiedadDTO> referidas = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_REFERENCE);
			if (referidas != null && !referidas.isEmpty()) {
				for (PropiedadDTO iProp : referidas) {
					List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(iProp.getLlaveTabla());
					if (relaciones != null && !relaciones.isEmpty()) {
						List<PedidoVentaCaracteristicaDTO> camposOpcionReferidos = new ArrayList<>();
						if (document.getCaracteristicas() == null) {
							// Por algun motivo raro toca iniciar con new Array y addall los resultados
							PedidoVentaCaracteristicaDTO aux = new PedidoVentaCaracteristicaDTO();
							aux.setValorOpcion(document.getLlaveTabla());
							List<PedidoVentaCaracteristicaDTO> listAux = new ArrayList<PedidoVentaCaracteristicaDTO>();
							listAux.add(aux);
							camposOpcionReferidos.addAll(campoService.listar2getApiCode(listAux, relaciones));
						} else {
							camposOpcionReferidos.addAll( document.getCaracteristicas().stream()
									.map(PedidoVentaCaracteristicaDTO::clone).collect(Collectors.toList()));
						}
						if (modificador != null && modificador.getCaracteristicas() != null) {
							camposOpcionReferidos.addAll(modificador.getCaracteristicas().stream()
									.map(PedidoVentaCaracteristicaDTO::clone).collect(Collectors.toList()));
						}
						List<PedidoVentaCaracteristicaDTO> camposReferidos = getFieldsFromOtherDocument(relaciones,
								camposOpcionReferidos);
						if (camposReferidos != null) {
							for (PedidoVentaCaracteristicaDTO iCampo : camposReferidos) {
								if (iCampo.getValorText() != null) {
									if (iCampo.getCampoDTO() == null)
										iCampo.setCampoDTO(fieldService.consultaXId(iCampo.getCampo()));
									String codeReplace = iCampo.getCampoDTO().getCodigo();
									if (iCampo.getTransaccionRegistro() != null)
										codeReplace = codeReplace + "(" + iCampo.getTransaccionRegistro() + ")";
									parameters = parameters + ConstantesGenerales.PUNTO_COMA_DOBLE + "R_"
											+ codeReplace + ConstantesGenerales.IGUAL
											+ formatToReplaceAll(iCampo, iCampo.getTransaccionRegistro());
								}
							}
						}
					}
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
										parameters = addParameterString(parameters, iRelacion, campo,
												campo.getCampoDTO().getCodigo(), "M");
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
		log.info("[] Parameters (%s)", parameters);
		return parameters;
	}

	private String addParameterString(String parameters, RelacionInternaDTO iRelacion,
			PedidoVentaCaracteristicaDTO campo, String codeReplace, String tipo) {
		parameters = parameters + ConstantesGenerales.PUNTO_COMA_DOBLE +  tipo + "_" + codeReplace
				+ ((iRelacion.getAuxiliar() != null) ? "(" + iRelacion.getAuxiliar() + ")" : "") 
				+ ConstantesGenerales.IGUAL + formatToReplaceAll(campo, iRelacion.getAuxiliar());
		if (campo.getValorOpcion() != null) {
			parameters = parameters + ConstantesGenerales.PUNTO_COMA_DOBLE +  tipo + "_" + codeReplace
					+ ((iRelacion.getAuxiliar() != null) ? "(" + iRelacion.getAuxiliar() + ")" : "") + "_KEY"
					+ ConstantesGenerales.IGUAL + campo.getValorOpcion();
		}
		return parameters;
	}

	private Date getDateWithTransformations(String texto) {
		Date result = new Date();
		if (texto.contains("(")) {
			// Ejemplo E_FECHA_XXX[-15D]
			String formulaTime = texto.substring(texto.indexOf("(") + 1, texto.length() - 2);
			long timeToAdd = 0;
			try {
				timeToAdd = Long.parseLong(formulaTime.substring(1));
			} catch (Exception e) {
				timeToAdd = 365 * 10 * 24 * 60 * 60 * 1000; // Si hay error le sumo 10 years
			}
			if (formulaTime.contains("-"))
				timeToAdd = timeToAdd * -1; // Si es negativo
			result = new Date(result.getTime() + timeToAdd * 24 * 60 * 60 * 1000);
		}
		return result;
	}

	/**
	 * 
	 * @param plantilla
	 * @param parametros
	 * @return
	 */
	private String generateOutputFile(String plantilla, String parametros) {
		if (parametros != null && !parametros.isEmpty()) {
			Map<String, Object> mapParams = SoftureUtil.createMaptoString(parametros);
			for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
				if(entry.getValue()!=null && entry.getValue().getClass().getName().compareTo("java.lang.String")==0) {
					// Esto lo hago porque el replace all no me funciona con parentesis
					String codeToEvaluate = "{{" + entry.getKey() +"}}";
					while (plantilla.contains(codeToEvaluate)) {
						plantilla = plantilla.replace(codeToEvaluate, (String) entry.getValue());											
					}
				}
			}
			plantilla = plantilla.replaceAll("\\{\\{[A-Za-z0-9_/():\\[\\]]*\\}\\}", "");
			if(plantilla.contains("$")) {
				StringWriter out = new StringWriter();
				try {
					Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
					Template t = new Template("templateName", plantilla, cfg);
					t.process(mapParams, out);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return out.toString();
			}
		}
		return plantilla;
	}

	/**
	 * Recibo unos campos y una relaciones, valido que campos cumplen con las
	 * relaciones (con el atributo campo), selecciono los campos que cumplen con
	 * alguna relacion y consulto las caracteristicas de esos campos que se usan en
	 * el api
	 * 
	 * Despues tomo los tipo proceso para volver a ejecutar este proceso, los que no
	 * son proceso los dejo como respuesta
	 * 
	 * @param relaciones
	 * @param fields
	 * @return
	 * @throws ServerException
	 */
	private List<PedidoVentaCaracteristicaDTO> getFieldsFromOtherDocument(List<RelacionInternaDTO> relaciones,
			List<PedidoVentaCaracteristicaDTO> fields) throws ServerException {
		if (relaciones == null || relaciones.isEmpty() || fields == null || fields.isEmpty())
			return null;
		List<PedidoVentaCaracteristicaDTO> camposEscogidos = null;
		List<PedidoVentaCaracteristicaDTO> fieldsInternal = null; // Campos que van cumpliendo con lo que queremos
		List<RelacionInternaDTO> relacionesValidadas = new ArrayList<RelacionInternaDTO>();

		// Filtro los campos que recibo y tienen que ver con una relacion
		// de paso les coloco el codigo en setTransaccionRegistro
		// No puedo borrarlos de una vez toca agregarlos a validadas para borrar despues
		for (RelacionInternaDTO iRelacion : relaciones) {
			for (PedidoVentaCaracteristicaDTO iField : fields) {
				// Si son el mismo campos
				if (iRelacion.getCampo().compareTo(iField.getCampo()) == 0) {
					if (fieldsInternal == null)
						fieldsInternal = new ArrayList<PedidoVentaCaracteristicaDTO>();
					/// Para logimax debo colcoarles codigos en el auxiliar y el el registro
					/// transaccion llevo esos codigos
					// if (iRelacion.getAuxiliar() == null || !fieldsInternal.contains(iField)) {
					// Quite el filtro por los auxiliares cuando son 2 de un mismo campo
					relacionesValidadas.add(iRelacion); // Esta relacion despues se va borrar por eso la adiciono
					iField.setTransaccionRegistro(iRelacion.getAuxiliar());
					fieldsInternal.add(iField.clone());
					break;// Antes no estaba este break no se porque
					// }
				}
			}
		}
		// fieldsInternal Tiene los campos que cumplen con las relaciones
		if (fieldsInternal != null) {
			// Dejo solo las realciones que no se han validado
			// Esto me toco hacerlo porque se descuadranban los array al remove la relacion
			List<RelacionInternaDTO> relacionesSinRepetir = new ArrayList<RelacionInternaDTO>();
			relacionesSinRepetir.addAll(relaciones);
			for (RelacionInternaDTO iRelacion : relacionesValidadas) {
				relacionesSinRepetir.remove(iRelacion);
			}
			// Consulto lso campos que me sirven para consultar
			List<PedidoVentaCaracteristicaDTO> fieldsRelation = campoService.listar2getApiCode(fieldsInternal,
					relacionesSinRepetir);
			if (fieldsRelation != null) {
				// Retiro los campos que no son proceso y los dejo como respuesta
				for (PedidoVentaCaracteristicaDTO iFRelation : fieldsRelation) {
					for (PedidoVentaCaracteristicaDTO iInternal : fieldsInternal) {
						if (iInternal.getValorOpcion() != null
								&& iInternal.getValorOpcion().compareTo(iFRelation.getDocumento()) == 0) {
							fieldsInternal.remove(iInternal);
							break;
						}
					}
				}
			}
			camposEscogidos = new ArrayList<PedidoVentaCaracteristicaDTO>();
			camposEscogidos.addAll(fieldsInternal);
			List<PedidoVentaCaracteristicaDTO> mailInternal = getFieldsFromOtherDocument(relacionesSinRepetir,
					fieldsRelation);
			if (mailInternal != null) {
				camposEscogidos.addAll(mailInternal);
			}
		}
		return camposEscogidos;
	}

	/**
	 * 
	 * @param serverName       Nombre del servidor
	 * @param body             Cuerpo de la peticion POST
	 * @param headerProperties
	 * @return
	 * @throws ServerException
	 */
	private String callApi(WebServiceDTO serverName, String body, Map<String, String> headerProperties)
			throws ServerException {
		URL url;
		try {
			url = new URL(serverName.getServidorNombre());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);

			if (headerProperties != null && headerProperties.size() != 0) {
				for (Entry<String, String> jugador : headerProperties.entrySet()) {
					String clave = jugador.getKey();
					String valor = jugador.getValue();
					con.setRequestProperty(clave, valor);
				}
			}
			
			int connectTimeOut = 5000;
			PropiedadDTO connectTimeOutValue =  Propiedades.obtenerParametro(serverName, Propiedades.API_CONNECT_TIMEOUT);
			if(connectTimeOutValue!=null) {
				try {
					connectTimeOut = Integer.valueOf(connectTimeOutValue.getValor());	
				} catch (NumberFormatException e) {
					log.info(e.toString());
				}
			}
			
			int readTimeOut = 60000;
			PropiedadDTO readTimeOutValue =  Propiedades.obtenerParametro(serverName, Propiedades.API_CONNECT_TIMEOUT);
			if(readTimeOutValue!=null) {
				try {
					readTimeOut = Integer.valueOf(readTimeOutValue.getValor());	
				} catch (NumberFormatException e) {
					log.info(e.toString());
				}
			}
			con.setConnectTimeout(connectTimeOut);
			con.setReadTimeout(readTimeOut);
			con.connect();

			// Send request
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			log.info("[%s] Body API\n%s", con.getURL().toString(),  body);
			wr.write(body.getBytes(StandardCharsets.UTF_8));
			wr.close();

			log.info("[%s] Procesando API status (%s)", con.getURL().toString(), con.getResponseCode());
			BufferedReader in = null;
			if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}

			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			con.disconnect();
			return content.toString();
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param service
	 * @param responseWS
	 * @param token
	 * @return
	 * @throws ServerException
	 */
	private String generateDocuments(WebServiceDTO service, String responseWS, String token) throws ServerException {
		List<PropiedadDTO> newTemplates = Propiedades.obtenerVariosParametro(service, Propiedades.API_NEW_DOCUMENT);
		List<PropiedadDTO> secondaryTemplates = Propiedades.obtenerVariosParametro(service,
				Propiedades.API_SECONDARY_DOCUMENT);
		if (newTemplates == null || newTemplates.isEmpty())
			return null;
		HashMap<String, List<PedidoVentaDTO>> mapWithDocuments = new HashMap<String, List<PedidoVentaDTO>>();
		// List<PedidoVentaDTO> documents = new ArrayList<>();
		HashMap<String, List<DocumentoPlantillaCaracteristicaDTO>> hmapTemplate = new HashMap<String, List<DocumentoPlantillaCaracteristicaDTO>>();
		HashMap<String, List<RelacionInternaDTO>> hmapRelaciones = new HashMap<String, List<RelacionInternaDTO>>();
		for (PropiedadDTO iProp : newTemplates) {
			final Matcher matcher = Pattern.compile(iProp.getMotivo()).matcher(responseWS);
			int iteratorPrimary = 0;
			while (matcher.find()) {
				iteratorPrimary++;
				addDocumentoToMap(mapWithDocuments, createNewDocument(hmapTemplate, hmapRelaciones, iProp.getValor(),
						iProp.getLlaveTabla(), matcher.group(1), token, null));
				// Envio a guardar los documentos secundarios
				if (secondaryTemplates != null && !secondaryTemplates.isEmpty()) {
					for (PropiedadDTO iProp2 : secondaryTemplates) {
						final Matcher matcherSecond = Pattern.compile(iProp2.getMotivo()).matcher(matcher.group(1));
						while (matcherSecond.find()) {
							addDocumentoToMap(mapWithDocuments,
									createNewDocument(hmapTemplate, hmapRelaciones, iProp2.getValor(),
											iProp2.getLlaveTabla(), matcherSecond.group(1), token,
											String.valueOf(iteratorPrimary)));
						}
					}
				}
			}
		}
		if (mapWithDocuments.isEmpty())
			return null;
		String result = "";
		for (Map.Entry<String, List<PedidoVentaDTO>> entry : mapWithDocuments.entrySet()) {
			List<PedidoVentaDTO> documentFromMap = entry.getValue();
			DocumentoPlantillaDTO templateDTO = templateService.consultaXId(entry.getKey());
			if (documentFromMap != null && !documentFromMap.isEmpty()) {
				String storageMassiveString = "<root>";
				int iteratorXml = 0;
				for (PedidoVentaDTO iDocument : documentFromMap) {
					iteratorXml++;
					storageMassiveString = storageMassiveString + "<" + formatStringXML(templateDTO.getCodigo()) + ">";
					storageMassiveString = storageMassiveString + "<" + formatStringXML(templateDTO.getCodigo())
							+ "_NUMID>" + String.valueOf(iteratorXml) + "</" + formatStringXML(templateDTO.getCodigo())
							+ "_NUMID>";
					for (PedidoVentaCaracteristicaDTO iFieldDocument : iDocument.getCaracteristicas()) {
						if (iFieldDocument.getValorText() != null) {
							storageMassiveString = storageMassiveString + "<"
									+ formatStringXML(iFieldDocument.getCampoDTO().getNombre()) + ">";
							storageMassiveString = storageMassiveString + iFieldDocument.getValorText();
							storageMassiveString = storageMassiveString + "</"
									+ formatStringXML(iFieldDocument.getCampoDTO().getNombre()) + ">";
						}
					}
					storageMassiveString = storageMassiveString + "</" + formatStringXML(templateDTO.getCodigo()) + ">";
				}
				storageMassiveString = storageMassiveString + "</root>";
				log.info("[%s] Escribiendo documento de carga masiva (%s)", templateDTO.getCodigo(),
						documentFromMap.size());
				result = result + uploadService.uploadFile(storageMassiveString.getBytes(), "Masiva.xml", token) + ";;";
			}
		}
		if (result.endsWith(";;"))
			result = result.substring(0, result.length() - 2);
		return result;
	}

	/**
	 * 
	 * @param text
	 * @return
	 */
	private String formatStringXML(String text) {
		if (text == null || text.compareTo("") == 0)
			return "EMPTY";
		return text.replaceAll(" ", "_");
	}

	/**
	 * 
	 * @param mapWithDocuments
	 * @param documentNew
	 */
	private void addDocumentoToMap(HashMap<String, List<PedidoVentaDTO>> mapWithDocuments, PedidoVentaDTO documentNew) {
		List<PedidoVentaDTO> documentFromMap = mapWithDocuments.get(documentNew.getPlantilla());
		if (documentFromMap == null)
			documentFromMap = new ArrayList<PedidoVentaDTO>();
		documentFromMap.add(documentNew);
		mapWithDocuments.put(documentNew.getPlantilla(), documentFromMap);
	}

	/**
	 * 
	 * @param hmapTemplate
	 * @param hmapRelaciones
	 * @param templateId
	 * @param propId
	 * @param textoApi
	 * @param token
	 * @param parentId
	 * @return
	 * @throws ServerException
	 */
	private PedidoVentaDTO createNewDocument(HashMap<String, List<DocumentoPlantillaCaracteristicaDTO>> hmapTemplate,
			HashMap<String, List<RelacionInternaDTO>> hmapRelaciones, String templateId, String propId, String textoApi,
			String token, String parentId) throws ServerException {
		// optimizando la consulta de campos de una plantilla
		List<DocumentoPlantillaCaracteristicaDTO> camposPlantilla = hmapTemplate.get(templateId);
		if (camposPlantilla == null) {
			camposPlantilla = fieldService.listarCamposPlantillaConComplementos(templateId, token);
			hmapTemplate.put(templateId, camposPlantilla);
		}
		// optimizando la consulta de relaciones de propiedades
		List<RelacionInternaDTO> relaciones = hmapRelaciones.get(propId);
		if (relaciones == null) {
			relaciones = relacionService.relacionesPropiedad(propId);
			hmapRelaciones.put(propId, relaciones);
		}

		PedidoVentaDTO nuevo = new PedidoVentaDTO();
		nuevo.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
		nuevo.setPlantilla(templateId);
		for (DocumentoPlantillaCaracteristicaDTO iCampo : camposPlantilla) {
			RelacionInternaDTO relacionApi = null;
			for (RelacionInternaDTO iRelacion : relaciones) {
				if (iCampo.getLlaveTabla().compareTo(iRelacion.getCampo()) == 0) {
					relacionApi = iRelacion;
					break;
				}
			}
			nuevo.getCaracteristicas().add(createField(iCampo, relacionApi, textoApi, parentId));
		}
		// Envio a guardar el documento para finalizar
		// return saveUpdateInactivateDocumentFunction.save(nuevo, token);
		// cambie de estratiegia para cargar archivo de carga masiva
		return nuevo;
	}

	/**
	 * La relacion puede ser nula porque no se a definido
	 * 
	 * @param campo
	 * @param relacion
	 * @param texto
	 * @param parentId
	 * @return
	 * @throws ServerException
	 */
	private PedidoVentaCaracteristicaDTO createField(DocumentoPlantillaCaracteristicaDTO campo,
			RelacionInternaDTO relacion, String texto, String parentId) throws ServerException {
		PedidoVentaCaracteristicaDTO nueva = new PedidoVentaCaracteristicaDTO();
		nueva.setCampo(campo.getLlaveTabla());
		nueva.setCampoDTO(campo);
		if (relacion != null) {
			if (relacion.getAuxiliar() == null) {
				nueva.setValorText(parentId);
			} else {
				final Matcher matcher = Pattern.compile(relacion.getAuxiliar()).matcher(texto);
				if (matcher.find()) {
					switch (campo.getFormato()) {
					case DocumentoPlantillaCaracteristicaDTO.FECHA:
						nueva.setValorFecha(SoftureUtil.toDate(matcher.group(1)));
						nueva.setValorText(SoftureUtil.formatDateMassiveFile(nueva.getValorFecha()));
						break;
					case DocumentoPlantillaCaracteristicaDTO.NUMERO:
						nueva.setValorNumero(new BigDecimal(matcher.group(1)));
						nueva.setValorText(nueva.getValorNumero().toPlainString());
						break;
					case DocumentoPlantillaCaracteristicaDTO.TEXTO:
						nueva.setValorText(matcher.group(1));
						break;
					case DocumentoPlantillaCaracteristicaDTO.PROCESO:
						nueva.setValorText(matcher.group(1));
						// Le coloque el valor en el texto pero no se si va a funcionar
						break;
					}
				}
			}
		}
		return nueva;
	}

	/**
	 * 
	 * @param iCampo
	 * @return
	 */
	private String formatToReplaceAll(PedidoVentaCaracteristicaDTO iCampo, String auxiliarFormat) {
		if (iCampo == null || iCampo.getCampoDTO() == null || iCampo.getValorText() == null)
			return "";
		switch (iCampo.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.FECHA:
			if (auxiliarFormat == null)
				return iCampo.getValorText();
			return SoftureUtil.formatWithParameter(iCampo.getValorFecha(), auxiliarFormat);
		case DocumentoPlantillaCaracteristicaDTO.NUMERO:
			/*
			 * text = text.replaceAll(Matcher.quoteReplacement("$"), "");// Existia un full
			 * error con los signso pesos en el pattern text = text.replace(".000000", "");
			 * // Para logimax los numero no debian ir con decimales text =
			 * text.replace(".00", ""); // Para logimax los numero no debian ir con
			 * decimales text = text.replace(",", ""); // Para logimax los numero no debian
			 * ir con decimales
			 */
			return String.valueOf(iCampo.getValorNumero().longValue());
		case DocumentoPlantillaCaracteristicaDTO.PROCESO:
		case DocumentoPlantillaCaracteristicaDTO.TEXTO:
			return iCampo.getValorText();
		default:
			return iCampo.getValorText();
		}
	}

	/**
	 * 
	 * @param service
	 * @param tokenAuthentication
	 * @return
	 */
	private Map<String, String> getHeaderProperties(WebServiceDTO service, String tokenAuthentication) {
		Map<String, String> result = null;
		if (service.getPropiedades() != null && !service.getPropiedades().isEmpty()) {
			result = new HashMap<>();
			for (PropiedadDTO iProp : service.getPropiedades()) {
				if (iProp.getKey().compareTo(Propiedades.API_HEADER) == 0) {
					result.put(iProp.getValor(), iProp.getMotivo());
				}
			}
		}
		if (tokenAuthentication != null) {
			if (result == null)
				result = new HashMap<>();
			String[] extractionToHeader = tokenAuthentication.split(ConstantesGenerales.PUNTO_COMA_DOBLE);
			for (String iExtraction : extractionToHeader) {
				int indexEqual = iExtraction.lastIndexOf(ConstantesGenerales.IGUAL);
				if (indexEqual > 0) {
					result.put(iExtraction.substring(0, indexEqual), iExtraction.substring(indexEqual + 1));
				}
			}
		}
		return result;
	}

	/**
	 * Coloca en un texto la URL y lineas aparte cada encabezado, termina colocando
	 * un titulo de Body
	 * 
	 * @param headers Lista de encabezados
	 * @param url     URL a la que se conecta el API
	 * @return
	 */
	private String writeHeadersAndUrl(Map<String, String> headers, String url, String parameters, String extractions) {
		String result = "URL\n\n " + url + "\n\nHeaders\n\n";
		if (headers != null && headers.size() != 0) {
			for (Entry<String, String> item : headers.entrySet()) {
				result = result + item.getKey() + " : " + item.getValue() + "\n\n";
			}
		}
		result = result + "\n\nParameters\n\n" + parameters + "\n\nExtractions\n\n" + extractions;
		return result + "\n\nBODY\n\n";
	}

}
