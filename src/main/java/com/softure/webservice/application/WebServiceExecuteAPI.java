package com.softure.webservice.application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_transaction.application.DocumentoTransaccionSvc;
import com.softure.document_transition.application.CallDocumentUpdateFromAutomatic;
import com.softure.java.services.ProcessTemplate;
import com.softure.java.services.SoftureUtil;
import com.softure.mail.application.MailSendMessageToAdminService;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.upload.application.UploadSvc;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceEjecucionDTO;

@Component
public class WebServiceExecuteAPI {

	private static Logger log = LoggerFactory.getLogger(WebServiceExecuteAPI.class);

	private static final String ERROR_EXTRAYENDO = "Error extrayendo el siguiente regular pattern (mira la funcion matches de Java String): ";

	@Autowired @Lazy 
	private DocumentoPlantillaSvc templateService;
	@Autowired @Lazy 
	private CallDocumentUpdateFromAutomatic documentAutomaticUpdateFunction;
	@Autowired @Lazy 
	private PropiedadSvc propiedadesSvc;
	@Autowired @Lazy 
	private PedidoVentaSvc documentSvc;
	@Autowired @Lazy 
	private UploadSvc uploadService;
	@Autowired @Lazy 
	private WebServiceSvc webServiceSvc;
	@Autowired @Lazy 
	private WebServiceEjecucionSvc webServiceEjecucionSvc;
	@Autowired @Lazy 
	private MailSendMessageToAdminService mensajeToAdminService;
	@Autowired @Lazy 
	private WebServiceCallPrepare prepareDataService;
	@Autowired @Lazy 
	private RelacionInternaSvc relacionService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired @Lazy 
	private ProcessTemplate templatesService;

	public void programateExecution(String pServiceId, String pDocumentId, String pModificadorId, String pTransactionId, String pToken) throws ServerException {
		WebServiceEjecucionDTO callWS = new WebServiceEjecucionDTO();
		callWS.setServicio(pServiceId);
		String userId = webServiceSvc.getUserFlex(pToken);
		callWS.setUsuario(userId);
		callWS.setFecha(new Date());
		callWS.setDocumento(pDocumentId);
		callWS.setTransaccion(pTransactionId);
		callWS.setSincrona(DocumentoTransaccionSvc.API_PREPARE_ASYNC);
		callWS.setModificador(pModificadorId);
		webServiceEjecucionSvc.save(callWS);
	}
	
	public String applyScheduleToExecute(WebServiceEjecucionDTO dto, String pToken) throws ServerException {
		
		prepareApiToExecution(dto.getServicio(), documentSvc.consultaXId(dto.getDocumento()), null, null, pToken, null);
		dto.setFechaEjecucion(new Date());
		//if (dto.getSincrona() != null) {
		//	dto.setSincrona(null);
		//}
		dto.setEstado(SharedConstants.STATE_INACTIVE);
		webServiceEjecucionSvc.update(dto);
		return dto.getLlaveTabla();
	}
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
	public String prepareApiToExecution(String serviceId, PedidoVentaDTO document, PedidoVentaDTO modificador, PedidoVentaDTO pIterador,
			String token, String previousParameter) throws ServerException {
		// Valido existencia del servicio
		WebServiceDTO service = webServiceSvc.consultaXId(serviceId);
		if (service == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + serviceId);
		if (service.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El servicio " + service.getNombre() + " no se encuentra Activo." + serviceId);
		// Obtengo propiedades del servicio
		String userId = webServiceSvc.getUserFlex(token);
		service.setPropiedades(
				propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE, serviceId, null, userId));
		// Inicia ejecucion
		log.info("[" + document.getNombre() + "] Procesando API (" + service.getNombre() + ")");
		WebServiceEjecucionDTO apiBasic = prepareDataService.call(service, document, modificador, pIterador, token, userId,
				previousParameter);
		String preValidation = propiedadesSvc.prevalidateAPI(service, apiBasic.getDocumento(),
				apiBasic.getModificador(), apiBasic.getParametros());
		if (preValidation != null) {
			apiBasic.setFechaEjecucion(new Date());
			apiBasic.setError(preValidation);
			webServiceEjecucionSvc.update(apiBasic);
			publishErrorMessage(service, apiBasic, modificador);
			log.info("[" + apiBasic.getDocumento() + "] Finalizando API (" + service.getNombre()
					+ ") por error de validacion previa a la ejecucion");
			return SharedConstants.ERROR;
		}
		String result = SharedConstants.OK;
		// En caso que la ejecucion sea asincrona omito call api
		if (Propiedades.obtenerParametro(service, Propiedades.API_ASYNCHRONOUS) == null) {
			result = executeApi(service, apiBasic, token, modificador, document, pIterador);
		} else {
			apiBasic.setSincrona(DocumentoTransaccionSvc.API_ASYNC);
			applyScheduleToExecute(apiBasic, service);
			//Cuando los parametros son muy grandes
			String parameterHelperToLong = null;
			if (apiBasic.getParametros() != null && apiBasic.getParametros().length() > 4000) {
				parameterHelperToLong = apiBasic.getParametros();
				apiBasic.setParametros(
						uploadService.uploadFile(parameterHelperToLong.getBytes(), "Parameter.txt", token, "webservice"));
			}
			
			webServiceEjecucionSvc.update(apiBasic);
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
			PedidoVentaDTO modificador, PedidoVentaDTO documentMain, PedidoVentaDTO pIterador) throws ServerException {
		if (callWS.getFechaEjecucion() != null)
			return SharedConstants.OK;
		if (service.getPropiedades() == null) {
			service.setPropiedades(propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE,
					service.getLlaveTabla(), null, null));
		}
		// Realizo la autenticacion
		String result = SharedConstants.OK;
		WebServiceEjecucionDTO preconditionWS = executePreviousWebService(service, callWS.getUsuario(), callWS.getDocumento(), token, modificador,
				documentMain, pIterador);
		String extractionApiPrecondition = null;		
		if (preconditionWS != null) {
			if (preconditionWS.getError() != null) {
				if (callWS.getSincrona() != null) {
					callWS.setSincrona(null);
				}
				callWS.setFechaEjecucion(new Date());
				callWS.setError(preconditionWS.getError());
				callWS.setParametersInexecution(getParametersWithHttp(callWS.getParametros()));
				webServiceEjecucionSvc.update(callWS);
				publishErrorMessage(service, preconditionWS, modificador);
				log.info("[" + callWS.getDocumento() + "] Finalizando API (" + service.getNombre()
						+ ") por error de API precondicion ");
				return SharedConstants.ERROR;
			}
			if (preconditionWS.getExtracciones() != null) 
				extractionApiPrecondition = preconditionWS.getExtracciones();
		}

		if (extractionApiPrecondition == null) {
			if(callWS.getParametersInexecution()==null) {
				callWS.setParametersInexecution(getParametersWithHttp(callWS.getParametros()));
			}
		} else {
			callWS.setParametersInexecution(getParametersWithHttp(callWS.getParametros()) + extractionApiPrecondition);
		}
		Map<String, String> headers = getHeaderProperties(service, callWS.getParametersInexecution());
		// Execution
		callWS = launchWebService(service, callWS, token, headers, modificador);
		// Primero intento de nuevo ejecutarlo
		if (callWS.getError() != null)
			callWS = tryAgain(service, callWS, token, 1, headers, modificador);
		// Si despues de todos los intentos no funciona ya se responde error
		if (callWS.getError() != null) {
			result = SharedConstants.ERROR;
			publishErrorMessage(service, callWS, modificador);
		} else {
			callWS.setMasivo(generateDocuments(service, callWS.getTextoRespuesta(), token));
			if (callWS.getMasivo() != null && callWS.getMasivo().compareTo("") != 0) {
				webServiceEjecucionSvc.update(callWS);
			}
		}
		log.info("[" + callWS.getDocumento() + "] Finalizando API (" + service.getNombre() + ")");
		return result;
	}
	
	private String getParametersWithHttp(String pParameters) {
		// Cuando los parametros son muy grandes y estan con http
		if (pParameters != null && pParameters.startsWith("http")) {
			try {
				File file = File.createTempFile("PARAMETER_", ".txt");
				FileUtils.copyURLToFile(new URL(pParameters), file);
				pParameters=  FileUtils.readFileToString(file, Charset.defaultCharset());
			} catch (IOException e) {
				e.printStackTrace();
				pParameters = pParameters + e.getMessage();
			}
		}
		return pParameters;
	}

	private void publishErrorMessage(WebServiceDTO service, WebServiceEjecucionDTO callWS, PedidoVentaDTO document) {

		try {
			String infoError = callWS.getError();
			infoError = infoError + "\nDocumento Principal: "
					+ documentSvc.consultaXId(callWS.getDocumento()).getNombre();
			if (callWS.getModificador() != null)
				infoError = infoError + "\nDocumento generador: "
						+ documentSvc.consultaXId(callWS.getModificador()).getNombre();
			infoError = infoError + "\nEntrada " + callWS.getEntrada();
			infoError = infoError + "\nRespuesta " + callWS.getSalida();
			infoError = infoError + "\n\nId " + callWS.getLlaveTabla() + " [" + SoftureUtil.formatDateTime(new Date())
					+ "]";
			CallDocumentCommons.addMessageError(document, callWS.getError());
			PropiedadDTO mailNotification = Propiedades.obtenerParametro(service, Propiedades.API_MAIL_NOTIFICATION);
			if (mailNotification == null) {
				mensajeToAdminService.call("Error en ejecucion de un API " + service.getNombre(), infoError);
			} else {
				mensajeToAdminService.call("Error en ejecucion de un API " + service.getNombre(), infoError,
						mailNotification.getValor());
			}

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
	private WebServiceEjecucionDTO executePreviousWebService(WebServiceDTO service, String callWSUser, String callWSDocument,
			String token, PedidoVentaDTO updater, PedidoVentaDTO documentMain, PedidoVentaDTO pIterador) throws ServerException {
		PropiedadDTO previousProp = Propiedades.obtenerParametro(service, Propiedades.API_AUTHENTICATION);
		if (previousProp == null)
			return null;
		WebServiceDTO previousEndPoint = webServiceSvc.consultaXId(previousProp.getValor());
		if (previousEndPoint == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + previousProp.getValor());
		previousEndPoint.setPropiedades(propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE,
				previousEndPoint.getLlaveTabla(), null, callWSUser));
		Map<String, String> headers = getHeaderProperties(previousEndPoint, null);
		// *****Execute
		if (documentMain == null) {
			documentMain = new PedidoVentaDTO();
			documentMain.setLlaveTabla(callWSDocument);
		}
		if (updater != null && updater.getLlaveTabla().compareTo(documentMain.getLlaveTabla()) == 0)
			documentMain.setNombre(updater.getNombre());
		WebServiceEjecucionDTO previousWS = prepareDataService.call(previousEndPoint, documentMain, updater, pIterador, token,
				callWSUser, null);
		return launchWebService(previousEndPoint, previousWS, token, headers, updater);
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

		// Reemplazos
		List<PropiedadDTO> replaceProperties = Propiedades.obtenerVariosParametro(service,
				Propiedades.API_CODE_REPLACE);
		callWS.setParametersInexecution( prepareParameterFromProperties(callWS.getParametersInexecution(), replaceProperties) );
		// Esto lo puedo unir con prepare, lo que sucede es que no quiero copiar en bd
		// los parametros fijos
		List<PropiedadDTO> properties = Propiedades.obtenerVariosParametro(service, Propiedades.API_BASE);
		if (properties != null && !properties.isEmpty()) {
			for (PropiedadDTO iProp : properties) {
				callWS.setParametersInexecution( prepareParameterFromProperties(callWS.getParametersInexecution(), propiedadesSvc.obtenerPropiedades(
						PropiedadValorDefinidoDTO.API_SERVICE, iProp.getValor(), Propiedades.API_CODE_REPLACE, null)));
			}
		}

		String template = templatesService.generateOutputFile(service.getTemplate(), callWS.getParametersInexecution());
		String urlWithParameters = templatesService.generateOutputFile(service.getUrl(), callWS.getParametersInexecution());
		// Se encontraba un error de codificacion asi que se debe pasar a UTF-8
		// if(template!=null) template = codifyToHTML(template);
		String fullOutput = writeHeadersAndUrl(headerProperties, urlWithParameters, callWS.getParametros(),
				callWS.getExtracciones(), service.getNombre()) + template;
		callWS.setEntrada(uploadService.uploadFile(fullOutput.getBytes(), "Entrada.txt", token, "webservice"));
		String responseApi = null;
		try {
			responseApi = callApi(service, urlWithParameters, template, headerProperties);
			callWS.setError(validateResultAPI(responseApi,
					Propiedades.obtenerVariosParametro(service, Propiedades.API_VALIDATION)));
			List<PropiedadDTO> extractionProperties = null;
			if (callWS.getError() == null) {
				String[] props = { Propiedades.API_EXTRACTION, Propiedades.API_EXTRACTION_NO_ERROR, Propiedades.API_EXTRACTION_TO_BASE_64 };
				extractionProperties = Propiedades.obtenerVariosParametro(service, props);
			} else {
				String[] props = { Propiedades.API_EXTRACTION_NO_ERROR };
				extractionProperties = Propiedades.obtenerVariosParametro(service,  props );
			}
				 
			List<String> resultExtraction = extractionResultAPI(responseApi, extractionProperties, token);
			String extractionString = "";
			for(String iExtraction: resultExtraction) {
				if (iExtraction.toUpperCase().startsWith("ERROR")) {
					if(callWS.getError() == null) {
						callWS.setError(iExtraction);
					}else {
						callWS.setError(callWS.getError()+ "\n\n"  + iExtraction);
					}
				} else {
					if(extractionString.isEmpty()) {
						extractionString =  iExtraction;
					}else {
						if(iExtraction.startsWith("INFO_")) {
							if(modificador!=null)CallDocumentCommons.addMessageError(modificador, iExtraction.substring(5));
						} else {
							extractionString = extractionString + SharedConstants.PUNTO_COMA_DOBLE+ iExtraction;							
						}
					}
				}
			}
			if (!extractionString.isEmpty()){
				callWS.setExtracciones(SharedConstants.PUNTO_COMA_DOBLE+ extractionString);
				// Esto lo puedo quitar con lso apis locales
				if (modificador != null )
					documentAutomaticUpdateFunction.executeFromAPIExtraction(modificador, extractionProperties,
							token, extractionString);
				
			}
			
			if (callWS.getError() != null) 
				responseApi = callWS.getError() + "\n\n" + responseApi;
			
		} catch (Exception e) {
			if (responseApi == null)
				responseApi = "";
			responseApi = e.getMessage() + "\n\n" + responseApi;
			callWS.setError(e.getMessage());
			log.info("[] Procesando API error (" + e.getMessage() + ")");
		}

		if (callWS.getExtracciones() != null)
			responseApi = "Extracciones\n\n" + callWS.getExtracciones() + "\n\n" + responseApi;
		callWS.setSalida(uploadService.uploadFile(responseApi.getBytes(), "Salida.txt", token, "webservice"));
		callWS.setFechaEjecucion(new Date());
		String extractionHelperToLong = null;
		if (callWS.getExtracciones() != null && callWS.getExtracciones().length() > 4000) {
			extractionHelperToLong = callWS.getExtracciones();
			callWS.setExtracciones(
					uploadService.uploadFile(extractionHelperToLong.getBytes(), "Extraction.txt", token, "webservice"));
		}
		if (callWS.getError() == null) callWS.setParametros(null);
		callWS = webServiceEjecucionSvc.update(callWS);
		callWS.setTextoRespuesta(responseApi);
		if (extractionHelperToLong != null)
			callWS.setExtracciones(extractionHelperToLong);
		//if (parameterHelperToLong != null)
		//	callWS.setParametros(parameterHelperToLong);
		return callWS;
	}

	public String prepareParameterFromProperties(String parameters, List<PropiedadDTO> replaceProperties)
			throws ServerException {
		if (replaceProperties != null && !replaceProperties.isEmpty()) {
			if (parameters == null)
				parameters = "";
			for (PropiedadDTO iProp : replaceProperties) {
				if (iProp.getTexto() == null)
					throw new ServerException(
							"Es necesario colocar texto en la propiedad de codigo a reemplazar " + iProp.getValor());
				parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + iProp.getTexto() + SharedConstants.IGUAL
						+ iProp.getValor();
			}
		}
		return parameters;
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
				if(propiedadDTO.getMotivo()==null) {
					return "Error validando el siguiente regular pattern (mira la funcion matches de Java String): "
							+ propiedadDTO.getValor();					
				} else {
					return "Error : "+ propiedadDTO.getMotivo();
				}
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
	private List<String> extractionResultAPI(String responseApi, List<PropiedadDTO> extractionList, String token)
			throws ServerException {
		if (extractionList == null || extractionList.isEmpty())
			return new ArrayList<>();
		List<String> result = new ArrayList<>();
		for (PropiedadDTO propiedadDTO : extractionList) {
			final Matcher matcher = Pattern.compile(propiedadDTO.getValor()).matcher(responseApi);
			if (!matcher.matches()) {
				if (propiedadDTO.getKey().compareTo(Propiedades.API_EXTRACTION_NO_ERROR) != 0) {
					if(propiedadDTO.getMotivo()==null) {
						result.add( ERROR_EXTRAYENDO + propiedadDTO.getValor() );						
					} else {
						result.add( "ERROR: " + propiedadDTO.getMotivo() );
					}
				}
			} else {
				String newValue = matcher.group(1);
				if (propiedadDTO.getKey().compareTo(Propiedades.API_EXTRACTION_TO_BASE_64) == 0) {
					newValue = uploadService.uploadFile(uploadService.transformBase64ToPDF(newValue),
							Propiedades.API_EXTRACTION_TO_BASE_64 + ".pdf", token, "webservice");
				}
				if (newValue != null && newValue.length() > 4000) {
					result.add( propiedadDTO.getLlaveTabla()+ SharedConstants.IGUAL + uploadService.uploadFile(newValue.getBytes(), "Extraction.txt", token, "webservice"));
				} else {
					result.add( propiedadDTO.getLlaveTabla()+ SharedConstants.IGUAL + newValue);
				}
				// debo colocar oble para que se guarden en formularios
				if (propiedadDTO.getTexto() != null)
					result.add( propiedadDTO.getTexto() + SharedConstants.IGUAL + newValue);
				
				//Estas no van en el calculo de las modificaciones
				if(propiedadDTO.getMotivo()!=null)
					result.add( "INFO_" + propiedadDTO.getMotivo() + SharedConstants.IGUAL + newValue);
			}
		}
		return result;
	}

	/**
	 * 
	 * @param apiService       Nombre del servidor
	 * @param body             Cuerpo de la peticion POST
	 * @param headerProperties
	 * @return
	 * @throws ServerException
	 */
	private String callApi(WebServiceDTO apiService, String url, String body, Map<String, String> headerProperties)
			throws ServerException {
		StringBuffer content = new StringBuffer();
		URL urlApi;
		HttpURLConnection con = null;
		try {
			urlApi = new URL(url);
			con = (HttpURLConnection) urlApi.openConnection();
			PropiedadDTO httpMethodValue = Propiedades.obtenerParametro(apiService,
					Propiedades.HTTP_METHOD);
			if (httpMethodValue != null) {
				con.setRequestMethod(httpMethodValue.getValor());
			}else {
				con.setRequestMethod("POST");
				con.setDoOutput(true);
			}
			

			if (headerProperties != null && headerProperties.size() != 0) {
				for (Entry<String, String> jugador : headerProperties.entrySet()) {
					String clave = jugador.getKey();
					String valor = jugador.getValue();
					con.setRequestProperty(clave, valor);
				}
			}

			int connectTimeOut = 5000;
			PropiedadDTO connectTimeOutValue = Propiedades.obtenerParametro(apiService,
					Propiedades.API_CONNECT_TIMEOUT);
			if (connectTimeOutValue != null) {
				try {
					connectTimeOut = Integer.valueOf(connectTimeOutValue.getValor());
				} catch (NumberFormatException e) {
					log.info(e.toString());
				}
			}

			int readTimeOut = 60000;
			PropiedadDTO readTimeOutValue = Propiedades.obtenerParametro(apiService, Propiedades.API_READ_TIMEOUT);
			if (readTimeOutValue != null) {
				try {
					readTimeOut = Integer.valueOf(readTimeOutValue.getValor());
				} catch (NumberFormatException e) {
					log.info(e.toString());
				}
			}
			con.setConnectTimeout(connectTimeOut);
			con.setReadTimeout(readTimeOut);
			con.connect();

			String standarEncoding = Propiedades.obtenerValor(apiService, Propiedades.API_ENCODE_STANDAR);
			if (standarEncoding.isEmpty())
				standarEncoding = StandardCharsets.UTF_8.toString();
			if(con.getRequestMethod().compareTo("GET")!=0) {
				// Send request
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				// log.info("[" + con.getURL().toString() + "] Body API\n" + body);
				// LA codificaion ISO_8859_1 es para soportar DIAn con tildes
				wr.write(body.getBytes(standarEncoding));
				wr.close();	
			}

			log.info("[" + con.getURL().toString() + "] Procesando API status (" + con.getResponseCode() + ")");
			BufferedReader in = null;
			if (100 <= con.getResponseCode() && con.getResponseCode() <= 399) {
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
		}  catch (SocketTimeoutException stoe) {
			throw new ServerException(stoe.getMessage() +".  RTO=" + con.getReadTimeout() + " CTO=" + con.getConnectTimeout());
		}
		catch (IOException e) {
			throw new ServerException(e.getMessage());
		}finally {
		    if(con != null) con.disconnect();
		}
		return content.toString();
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
				log.info("[" + templateDTO.getCodigo() + "] Escribiendo documento de carga masiva ("
						+ documentFromMap.size() + ")");
				result = result
						+ uploadService.uploadFile(storageMassiveString.getBytes(), "Masiva.xml", token, "webservice")
						+ ";;";
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
			camposPlantilla = fieldService.listarCamposPlantillaConComplementos(templateId, token, false);
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
						Date dateParsed = SoftureUtil.toDate(matcher.group(1));
						if (dateParsed == null)
							throw new ServerException(
									"El valor " + matcher.group(1) + " no se pudo identificar como una fecha");
						nueva.setValorFecha(dateParsed);
						nueva.setValorText(SoftureUtil.formatDateMassiveFile(nueva.getValorFecha()));
						break;
					case DocumentoPlantillaCaracteristicaDTO.NUMERO:
						try {
							String valueToFormat = matcher.group(1);
							// EN universal con TCC sucedia que enviaban decimales con comas y esto genera
							// error
							if (valueToFormat.contains(","))
								valueToFormat.replace(",", ".");
							nueva.setValorNumero(new BigDecimal(valueToFormat));
							nueva.setValorText(nueva.getValorNumero().toPlainString());
						} catch (Exception e) {
							nueva.setValorNumero(BigDecimal.ZERO);
							nueva.setValorText(e.getMessage());
						}
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
					result.put(iProp.getValor(),
							templatesService.generateOutputFile(iProp.getMotivo(), tokenAuthentication));
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
	private String writeHeadersAndUrl(Map<String, String> headers, String url, String parameters, String extractions,
			String name) {
		String result = "URL\n " + url + "\n\nName\n " + name + "\n\nHeaders\n\n";
		if (headers != null && headers.size() != 0) {
			for (Entry<String, String> item : headers.entrySet()) {
				result = result + item.getKey() + " : " + item.getValue() + "\n\n";
			}
		}
		if (parameters != null) {
			result = result + "\n\nParameters\n\n";
			result = result +  parameters.replaceAll(";;", "\n").replaceAll("--", "\n\t").replaceAll(",,", "=");
		}
		if (extractions != null) {
			result = result + "\n\nExtractions\n\n";
			result = result +  extractions.replaceAll(";;", "\n").replaceAll("--", "\n\t").replaceAll(",,", "=");
		}
		return result + "\n\nBODY\n\n";
	}

	private void applyScheduleToExecute(WebServiceEjecucionDTO apiBasic, WebServiceDTO service) {
		List<PropiedadDTO> schedule = Propiedades.obtenerVariosParametro(service, Propiedades.API_SCHEDULE_TIME_BLOCK);
		if (schedule == null || schedule.isEmpty())
			return;
		// Valido bloqueo por tiempo
		for (PropiedadDTO iPropiedadDTO : schedule) {
			apiBasic.setFecha(Propiedades.getNextDateTimeSchedule(iPropiedadDTO.getValor(), apiBasic.getFecha()));
		}
	}
}
