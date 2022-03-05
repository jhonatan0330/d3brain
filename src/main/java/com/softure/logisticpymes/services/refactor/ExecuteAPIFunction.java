package com.softure.logisticpymes.services.refactor;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.dto.WebServiceDTO;
import com.softure.logisticpymes.dto.WebServiceEjecucionDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.PropiedadSvc;
import com.softure.logisticpymes.services.RelacionInternaSvc;
import com.softure.logisticpymes.services.UploadSvc;
import com.softure.logisticpymes.services.WebServiceEjecucionSvc;
import com.softure.logisticpymes.services.WebServiceSvc;
import com.softure.logisticpymes.services.adapter.Propiedades;

@Component
public class ExecuteAPIFunction {

	@Autowired
	private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired
	private DocumentNewSaveUpdateInactivateFunction saveUpdateInactivateDocumentFunction;
	@Autowired
	private DocumentAutomaticUpdateFunction documentAutomaticUpdateFunction;
	@Autowired
	private PropiedadSvc propiedadesSvc;
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

	public String execute(String serviceId, PedidoVentaDTO document, PedidoVentaDTO modificador, String token)
			throws ServerException {
		String result = ConstantesGenerales.OK;
		WebServiceDTO service = webServiceSvc.consultaXId(serviceId);
		if (service == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + serviceId);
		System.out.format("\n\n[%s] Procesando API (%s)", document.getNombre(), service.getNombre());
		String userId = webServiceSvc.getUserFlex(token);
		service.setPropiedades(
				propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE, serviceId, null, userId));
		// authentication
		WebServiceEjecucionDTO authenticationWS = executeAuthenticationWebService(service, document, modificador, token,
				userId);
		String tokenAuthentication = null;
		if (authenticationWS != null) {
			if (authenticationWS.getError() != null) {
				System.out.format("\n\n[%s] Finalizando API (%s) por error de autenticacion", document.getNombre(),
						service.getNombre());
				return ConstantesGenerales.ERROR;
			}
			tokenAuthentication = authenticationWS.getSalida();
		}
		Map<String, String> headers = getHeaderProperties(service, tokenAuthentication);
		// Execution
		WebServiceEjecucionDTO callWS = launchWebService(service, document, modificador, token, userId, headers);
		// Primero intento de nuevo ejecutarlo
		if (callWS.getError() != null)
			callWS = tryAgain(service, callWS, document, modificador, token, userId, 1, headers);
		// Si despues de todos los intentos no funciona ya se responde error
		if (callWS.getError() != null) {
			result = ConstantesGenerales.ERROR;
		} else {
			generateDocuments(service, callWS.getEntrada(), document, token);
		}
		System.out.format("\n\n[%s] Finalizando API (%s)", document.getNombre(), service.getNombre());
		return result;
	}

	private WebServiceEjecucionDTO executeAuthenticationWebService(WebServiceDTO service, PedidoVentaDTO document,
			PedidoVentaDTO modificador, String token, String userId) throws ServerException {
		PropiedadDTO authenticationProp = Propiedades.obtenerParametro(service, Propiedades.API_AUTHENTICATION);
		if (authenticationProp == null)
			return null;
		WebServiceDTO authenticationEndPoint = webServiceSvc.consultaXId(authenticationProp.getValor());
		if (authenticationEndPoint == null)
			throw new ServerException("El id del servicio no se encuentra en la BD." + authenticationProp.getValor());
		authenticationEndPoint.setPropiedades(propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE,
				authenticationEndPoint.getLlaveTabla(), null, userId));
		Map<String, String> headers = getHeaderProperties(authenticationEndPoint, null);
		// *****Execute
		WebServiceEjecucionDTO authenticationWS = launchWebService(authenticationEndPoint, document, modificador, token,
				userId, headers);
		// Esto esta quemado para mensajes de texto toca pensar la mejor estrategia
		for (PropiedadDTO propiedadDTO : authenticationEndPoint.getPropiedades()) {
			if (propiedadDTO.getKey().compareTo(Propiedades.API_EXTRACTION) == 0) {
				authenticationWS.setSalida("Bearer " + propiedadDTO.getTexto());
				break;
			}
		}

		return authenticationWS;
	}

	private WebServiceEjecucionDTO launchWebService(WebServiceDTO service, PedidoVentaDTO document,
			PedidoVentaDTO modificador, String token, String userId, Map<String, String> headerProperties)
			throws ServerException {
		WebServiceEjecucionDTO callWS = new WebServiceEjecucionDTO();
		callWS.setServicio(service.getLlaveTabla());
		callWS.setFecha(new Date());
		String template = generateRequestBody(service, document, modificador);
		String fullOutput = writeHeaders(headerProperties) + template;
		callWS.setEntrada(uploadService.uploadFile(fullOutput.getBytes(), "Entrada.txt", token));
		callWS.setDocumento(document.getLlaveTabla());
		callWS.setUsuario(userId);
		String responseApi = null;
		try {
			responseApi = callApi(service, callWS, template, headerProperties);
			validateResultAPI(responseApi, service, callWS);
			if (callWS.getError() == null) {
				List<PropiedadDTO> extractionProperties = extractionResultAPI(responseApi, service, callWS);
				documentAutomaticUpdateFunction.executeFromAPIExtraction(document.getLlaveTabla(),
						modificador.getLlaveTabla(), extractionProperties, token, modificador.getTransaccion());
			}
		} catch (Exception e) {
			if (responseApi == null)
				responseApi = "";
			responseApi = e.getMessage() + "\n\n" + responseApi;
			callWS.setError(e.getMessage());
			System.out.format("\n[] Procesando API error (%s)", e.getMessage());
		}
		callWS.setSalida(uploadService.uploadFile(responseApi.getBytes(), "Salida.txt", token));
		callWS = webServiceEjecucionSvc.save(callWS);
		callWS.setEntrada(responseApi);
		return callWS;
	}

	private WebServiceEjecucionDTO tryAgain(WebServiceDTO service, WebServiceEjecucionDTO callWS,
			PedidoVentaDTO document, PedidoVentaDTO modificador, String token, String userId, int countIteration,
			Map<String, String> headers) throws ServerException {
		PropiedadDTO tryProp = Propiedades.obtenerParametro(service, Propiedades.API_MAX_TRY);
		if (tryProp == null)
			return callWS;
		try {
			int maxTry = Integer.parseInt(tryProp.getValor());
			if (countIteration < maxTry && countIteration < 3) {
				callWS = launchWebService(service, document, modificador, token, userId, headers);
				if (callWS.getError() != null)
					callWS = tryAgain(service, callWS, document, modificador, token, userId, countIteration + 1,
							headers);
			}
		} catch (NumberFormatException e) {
		}
		return callWS;
	}

	private void validateResultAPI(String responseApi, WebServiceDTO service, WebServiceEjecucionDTO callWS) {
		List<PropiedadDTO> validationProperties = Propiedades.obtenerVariosParametro(service,
				Propiedades.API_VALIDATION);
		if (validationProperties == null || validationProperties.isEmpty())
			return;
		for (PropiedadDTO propiedadDTO : validationProperties) {
			if (!responseApi.matches(propiedadDTO.getValor())) {
				String errorMatch = "Error validando el siguiente regular pattern (mira la funcion matches de Java String): "
						+ propiedadDTO.getValor();
				callWS.setError(errorMatch);
				responseApi = errorMatch + "\n\n" + responseApi;
				return;
			}
		}
	}

	private List<PropiedadDTO> extractionResultAPI(String responseApi, WebServiceDTO service,
			WebServiceEjecucionDTO callWS) throws ServerException {
		List<PropiedadDTO> extractionProperties = Propiedades.obtenerVariosParametro(service,
				Propiedades.API_EXTRACTION);
		if (extractionProperties == null || extractionProperties.isEmpty())
			return null;
		for (PropiedadDTO propiedadDTO : extractionProperties) {
			final Matcher matcher = Pattern.compile(propiedadDTO.getValor()).matcher(responseApi);
			if (!matcher.matches()) {
				String errorMatch = "Error extrayendo el siguiente regular pattern (mira la funcion matches de Java String): "
						+ propiedadDTO.getValor();
				callWS.setError(errorMatch);
				responseApi = errorMatch + "\n\n" + responseApi;
				return null;
			}
			propiedadDTO.setTexto(matcher.group(1));
		}
		return extractionProperties;
	}

	private String generateRequestBody(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador)
			throws ServerException {
		String template = service.getTemplate();
		if (service.getPropiedades() != null && !service.getPropiedades().isEmpty()) {
			// Directas
			List<PropiedadDTO> directas = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_DIRECT);
			if (directas != null && !directas.isEmpty()) {
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
								PedidoVentaCaracteristicaDTO campo = DocumentCommonsFunction
										.obtenerValor(camposOpcionales, iRelacion.getCampo());
								if (campo != null && campo.getValorText() != null) {
									if (campo.getCampoDTO() == null)
										campo.setCampoDTO(fieldService.consultaXId(campo.getCampo()));
									String codeReplace = (campo.getTransaccionRegistro() == null)
											? campo.getCampoDTO().getCodigo()
											: campo.getTransaccionRegistro();
									template = template.replaceAll("\\{\\{D_" + codeReplace + "\\}\\}",
											formatToReplaceAll(campo));
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
						template = template.replaceAll("\\{\\{" + iProp.getTexto() + "\\}\\}",
								SoftureUtil.formatDatePattern(new Date(), iProp.getValor()));
					} else {
						switch (iProp.getTexto()) {
						case "E_ID":
							template = template.replaceAll("\\{\\{" + iProp.getTexto() + "\\}\\}",
									document.getLlaveTabla());
							break;
						case "E_CODE":
							template = template.replaceAll("\\{\\{" + iProp.getTexto() + "\\}\\}",
									document.getNombre());
							break;
						default:
							template = template.replaceAll("\\{\\{" + iProp.getTexto() + "\\}\\}", iProp.getValor());
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
						List<PedidoVentaCaracteristicaDTO> camposReferidos = getFieldsFromOtherDocument(relaciones,
								camposOpcionales);
						if (camposReferidos != null) {
							for (PedidoVentaCaracteristicaDTO iCampo : camposReferidos) {
								if (iCampo.getValorText() != null) {
									if (iCampo.getCampoDTO() == null)
										iCampo.setCampoDTO(fieldService.consultaXId(iCampo.getCampo()));
									String codeReplace = (iCampo.getTransaccionRegistro() == null)
											? iCampo.getCampoDTO().getCodigo()
											: iCampo.getTransaccionRegistro();
									template = template.replaceAll("\\{\\{R_" + codeReplace + "\\}\\}",
											formatToReplaceAll(iCampo));
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
									PedidoVentaCaracteristicaDTO campo = DocumentCommonsFunction
											.obtenerValor(camposOpcionales, iRelacion.getCampo());
									if (campo != null && campo.getValorText() != null) {
										if (campo.getCampoDTO() == null)
											campo.setCampoDTO(fieldService.consultaXId(campo.getCampo()));
										template = template.replaceAll(
												"\\{\\{M_" + campo.getCampoDTO().getCodigo() + "\\}\\}",
												campo.getValorText());
									}
								}
							}
						}
					}
				}
			}
		}
		template = template.replaceAll("\\{\\{[A-Za-z0-9_]*\\}\\}", "");
		byte[] bytes = template.getBytes(StandardCharsets.UTF_8);
		return new String(bytes, StandardCharsets.UTF_8);
	}

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
				//Si son el mismo campos
				if (iRelacion.getCampo().compareTo(iField.getCampo()) == 0) {
					if (fieldsInternal == null)
						fieldsInternal = new ArrayList<PedidoVentaCaracteristicaDTO>();
					/// Para logimax debo colcoarles codigos en el auxiliar y el el registro transaccion llevo esos codigos
					if (iRelacion.getAuxiliar() == null || !fieldsInternal.contains(iField)) {
						relacionesValidadas.add(iRelacion); // Esta relacion despues se va borrar por eso la adiciono
						iField.setTransaccionRegistro(iRelacion.getAuxiliar());
						fieldsInternal.add(iField);
						break;// Antes no estaba este break no se porque
					}
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

	private String callApi(WebServiceDTO service, WebServiceEjecucionDTO callWS, String template,
			Map<String, String> headerProperties) throws ServerException {
		URL url;
		try {
			url = new URL(service.getServidorNombre());
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
			con.setConnectTimeout(5000);
			con.setReadTimeout(60000);
			con.connect();

			// Send request
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.write(template.getBytes(StandardCharsets.UTF_8));
			wr.close();

			System.out.format("\n[] Procesando API status (%s)", con.getResponseCode());

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

	private void generateDocuments(WebServiceDTO service, String responseWS, PedidoVentaDTO document, String token)
			throws ServerException {
		List<PropiedadDTO> newTemplates = Propiedades.obtenerVariosParametro(service, Propiedades.API_NEW_DOCUMENT);
		List<PropiedadDTO> secondaryTemplates = Propiedades.obtenerVariosParametro(service,
				Propiedades.API_SECONDARY_DOCUMENT);
		if (newTemplates != null && !newTemplates.isEmpty()) {
			HashMap<String, List<DocumentoPlantillaCaracteristicaDTO>> hmapTemplate = new HashMap<String, List<DocumentoPlantillaCaracteristicaDTO>>();
			HashMap<String, List<RelacionInternaDTO>> hmapRelaciones = new HashMap<String, List<RelacionInternaDTO>>();
			for (PropiedadDTO iProp : newTemplates) {
				final Matcher matcher = Pattern.compile(iProp.getMotivo()).matcher(responseWS);
				while (matcher.find()) {
					PedidoVentaDTO nuevo = createNewDocument(hmapTemplate, hmapRelaciones, iProp.getValor(),
							iProp.getLlaveTabla(), matcher.group(1), document, token);
					// Envio a guardar los documentos secundarios
					if (secondaryTemplates != null && !secondaryTemplates.isEmpty()) {
						for (PropiedadDTO iProp2 : secondaryTemplates) {
							final Matcher matcherSecond = Pattern.compile(iProp2.getMotivo()).matcher(matcher.group(1));
							while (matcherSecond.find()) {
								createNewDocument(hmapTemplate, hmapRelaciones, iProp2.getValor(),
										iProp2.getLlaveTabla(), matcherSecond.group(1), nuevo, token);
							}

						}
					}
				}
			}
		}
	}

	private PedidoVentaDTO createNewDocument(HashMap<String, List<DocumentoPlantillaCaracteristicaDTO>> hmapTemplate,
			HashMap<String, List<RelacionInternaDTO>> hmapRelaciones, String templateId, String propId, String textoApi,
			PedidoVentaDTO documentoPadre, String token) throws ServerException {
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
		nuevo.setTransaccion(documentoPadre.getTransaccion());
		for (DocumentoPlantillaCaracteristicaDTO iCampo : camposPlantilla) {
			RelacionInternaDTO relacionApi = null;
			for (RelacionInternaDTO iRelacion : relaciones) {
				if (iCampo.getLlaveTabla().compareTo(iRelacion.getCampo()) == 0) {
					relacionApi = iRelacion;
					break;
				}
			}
			nuevo.getCaracteristicas().add(createField(iCampo, relacionApi, textoApi, documentoPadre.getLlaveTabla()));
		}
		// Envio a guardar el documento para finalizar
		return saveUpdateInactivateDocumentFunction.save(nuevo, token);
	}

	// La relacion puede ser nula porque no se a definido
	private PedidoVentaCaracteristicaDTO createField(DocumentoPlantillaCaracteristicaDTO campo,
			RelacionInternaDTO relacion, String texto, String documento) throws ServerException {
		PedidoVentaCaracteristicaDTO nueva = new PedidoVentaCaracteristicaDTO();
		nueva.setCampo(campo.getLlaveTabla());
		if (relacion != null) {
			if (relacion.getAuxiliar() == null) {
				nueva.setValorOpcion(documento);
			} else {
				final Matcher matcher = Pattern.compile(relacion.getAuxiliar()).matcher(texto);
				if (matcher.find()) {
					switch (campo.getFormato()) {
					case DocumentoPlantillaCaracteristicaDTO.FECHA:
						nueva.setValorFecha(SoftureUtil.toDate(matcher.group(1)));
						break;
					case DocumentoPlantillaCaracteristicaDTO.NUMERO:
						nueva.setValorNumero(new BigDecimal(matcher.group(1)));
						break;
					case DocumentoPlantillaCaracteristicaDTO.TEXTO:
						nueva.setValorText(matcher.group(1));
						break;
					case DocumentoPlantillaCaracteristicaDTO.PROCESO:
						// Todavia no se que hacer
						break;
					}
				}
			}
		}
		return nueva;
	}

	private String formatToReplaceAll(PedidoVentaCaracteristicaDTO iCampo) {
		if (iCampo == null || iCampo.getCampoDTO() == null || iCampo.getValorText() == null)
			return "";
		switch (iCampo.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.NUMERO:
			/*
			 * text = text.replaceAll(Matcher.quoteReplacement("$"), "");// Existia un full
			 * error con los signso pesos en el pattern text = text.replace(".000000", "");
			 * // Para logimax los numero no debian ir con decimales text =
			 * text.replace(".00", ""); // Para logimax los numero no debian ir con
			 * decimales text = text.replace(",", ""); // Para logimax los numero no debian
			 * ir con decimales
			 */
			return String.valueOf(iCampo.getValorNumero().intValue());
		case DocumentoPlantillaCaracteristicaDTO.PROCESO:
		case DocumentoPlantillaCaracteristicaDTO.TEXTO:
			return iCampo.getValorText();
		default:
			return iCampo.getValorText();
		}
	}

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
			result.put("Authorization", tokenAuthentication);
		}
		return result;
	}

	private String writeHeaders(Map<String, String> headers) {
		String result = "Headers\n\n";
		if (headers != null && headers.size() != 0) {
			for (Entry<String, String> item : headers.entrySet()) {
				result = result + item.getKey() + " : " + item.getValue() + "\n\n";
			}
		}

		return result + "\n\nBODY\n\n";
	}

}
