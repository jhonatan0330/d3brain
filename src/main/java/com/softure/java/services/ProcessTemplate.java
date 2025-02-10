package com.softure.java.services;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Period;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.DetallePedidoVentaSvc;
import com.softure.document_execution.application.DocumentoRelacionExpedienteSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.mail.application.MailSendMessageToAdminService;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
public class ProcessTemplate {

	@Autowired
	@Lazy
	private DocumentoRelacionExpedienteSvc documentsInFieldService;
	@Autowired
	@Lazy
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired
	@Lazy
	private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired
	@Lazy
	private RelacionInternaSvc relacionService;
	@Autowired
	@Lazy
	private PedidoVentaSvc documentService;
	@Autowired
	@Lazy
	private DetallePedidoVentaSvc detallePedidoVentaService;
	@Autowired @Lazy 
	private MailSendMessageToAdminService sendToAdminService;

	public String generateOutputFile(String plantilla, String parametros) {
		if (parametros != null && !parametros.isEmpty()) {
			Map<String, Object> mapParams = SoftureUtil.createMaptoString(parametros);
			for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
				if (entry.getValue() != null
						&& entry.getValue().getClass().getName().compareTo("java.lang.String") == 0) {
					// Esto lo hago porque el replace all no me funciona con parentesis
					String codeToEvaluate = "{{" + entry.getKey() + "}}";
					while (plantilla.contains(codeToEvaluate)) {
						plantilla = plantilla.replace(codeToEvaluate, (String) entry.getValue());
					}
				}
			}
			plantilla = plantilla.replaceAll("\\{\\{[A-Za-z0-9_/():\\-\\[\\]]*\\}\\}", "");
			if (plantilla.contains("$")) {
				Map<String, Object> newMap = new HashMap<String, Object>();
				// En fremarker sale error con los parentesis
				for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
					if (entry.getKey().contains("(")) {
						String newKey = entry.getKey();
						while (newKey.contains("(")) {
							newKey = entry.getKey().replace("(", "_").replace(")", "").replace(":", "_")
									.replace("/", "_").replace("-", "_");
						}
						newMap.put(newKey, entry.getValue());
						// mapParams.remove(entry.getKey());
						// Por el momento no borro las entradas para una proxima
					}
				}
				mapParams.putAll(newMap);
				StringWriter out = new StringWriter();
				try {
					Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
					Template t = new Template("templateName", plantilla, cfg);
					t.process(mapParams, out);
				} catch (Exception e) {
					try {
						sendToAdminService.call("Error procesando una pantilla",
								e.getMessage() + SharedConstants.NEW_LINE +plantilla  );
					} catch (ServerException e1) {
						e.printStackTrace();
						e1.printStackTrace();
					}
				}
				return out.toString();
			}
		}
		return plantilla;
	}

	public String extractParameterTypeR(List<PropiedadDTO> referidas, PedidoVentaDTO document,
			PedidoVentaDTO modificador, String parameters, PropiedadDTO iProp) throws ServerException {
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
				camposOpcionReferidos.addAll(document.getCaracteristicas().stream()
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
						parameters = addParameterString(parameters, null, iCampo, codeReplace, "R",
								iCampo.getTransaccionRegistro(), referidas);
					}
				}
			}
		}
		return parameters;
	}

	public String addParameterString(String parameters, RelacionInternaDTO iRelacion,
			PedidoVentaCaracteristicaDTO campo, String codeReplace, String tipo, String formatToField,
			List<PropiedadDTO> referidas) throws ServerException {
		String valueAuxToCode = "";
		if (iRelacion != null) {
			if (iRelacion.getAuxiliar() != null && !iRelacion.getAuxiliar().isEmpty())
				valueAuxToCode = "(" + iRelacion.getAuxiliar() + ")";
		}
		parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + tipo + "_" + codeReplace + valueAuxToCode
				+ SharedConstants.IGUAL + formatToReplaceAll(campo, formatToField);
		if (campo.getValorOpcion() != null) {
			parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + tipo + "_" + codeReplace + valueAuxToCode
					+ "_KEY" + SharedConstants.IGUAL + campo.getValorOpcion();
			if (campo.getExpedientes() != null && !campo.getExpedientes().isEmpty()) {
				PedidoVentaDTO iElement = campo.getExpedientes().get(0);
				if (iElement != null && iElement.getNombre() != null) {
					parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + tipo + "_" + codeReplace
							+ valueAuxToCode + "_ID" + SharedConstants.IGUAL + iElement.getNombre();
				}
			}
		} else {
			if (referidas != null && !referidas.isEmpty()) {
				Map<String, List<RelacionInternaDTO>> relations = null;
				if (campo.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO) == 0) {
					if (campo.getExpedientes() == null) {
						List<DocumentoRelacionExpedienteDTO> documentsInField = documentsInFieldService
								.listByField(campo.getLlaveTabla());
						if (documentsInField != null && !documentsInField.isEmpty()) {
							relations = getRealationsRelatedWithList(referidas, relations);
							List<PropiedadDTO> propertiesWithRelationField = getPropertiesWithRelation(campo, referidas,
									relations);
							if (propertiesWithRelationField != null && !propertiesWithRelationField.isEmpty()) {
								for (int i = 0; i < documentsInField.size(); i++) {
									DocumentoRelacionExpedienteDTO iRelation = documentsInField.get(i);
									parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "I_" + codeReplace
											+ valueAuxToCode + "[" + String.valueOf(i + 1) + "]="
											+ SharedConstants.LINEA_MEDIA_DOBLE + "L_NUM" + SharedConstants.COMA_DOBLE
											+ String.valueOf(i + 1) + SharedConstants.LINEA_MEDIA_DOBLE + "L_VAL"
											+ SharedConstants.COMA_DOBLE + iRelation.getValor().intValue();
									for (PropiedadDTO iProp : propertiesWithRelationField) {
										if (iProp.getMotivo() != null) {
											parameters = parameters + SharedConstants.LINEA_MEDIA_DOBLE + "L" + "_"
													+ iProp.getMotivo() + SharedConstants.COMA_DOBLE + documentService
															.consultaXId(iRelation.getExpedienteDetalle()).getNombre();
										}
										List<RelacionInternaDTO> relaciones = relations.get(iProp.getLlaveTabla());
										if (relaciones != null && !relaciones.isEmpty()) {
											// Todo esto practimanete lo copie de la funcion de arriba de referidos
											List<PedidoVentaCaracteristicaDTO> camposOpcionReferidos = new ArrayList<>();
											PedidoVentaCaracteristicaDTO aux = new PedidoVentaCaracteristicaDTO();
											aux.setValorOpcion(iRelation.getExpedienteDetalle());
											List<PedidoVentaCaracteristicaDTO> listAux = new ArrayList<PedidoVentaCaracteristicaDTO>();
											listAux.add(aux);
											camposOpcionReferidos
													.addAll(campoService.listar2getApiCode(listAux, relaciones));
											List<PedidoVentaCaracteristicaDTO> camposReferidos = getFieldsFromOtherDocument(
													relaciones, camposOpcionReferidos);
											if (camposReferidos != null) {
												for (PedidoVentaCaracteristicaDTO iCampo : camposReferidos) {
													if (iCampo.getValorText() != null) {
														if (iCampo.getCampoDTO() == null)
															iCampo.setCampoDTO(
																	fieldService.consultaXId(iCampo.getCampo()));
														String codeReplaceList = iCampo.getCampoDTO().getCodigo();
														if (iCampo.getTransaccionRegistro() != null)
															codeReplaceList = codeReplaceList + "("
																	+ iCampo.getTransaccionRegistro() + ")";
														// +ConstantesGenerales.LINEA_MEDIA_DOBLE
														// +"GUIA"+ConstantesGenerales.COMA_DOBLE+"CT100"
														parameters = parameters + SharedConstants.LINEA_MEDIA_DOBLE
																+ "L" + "_" + codeReplaceList
																+ SharedConstants.COMA_DOBLE + formatToReplaceAll(
																		iCampo, iCampo.getTransaccionRegistro());
														// Coloque el service en null para evitar que se generen ciclos
														// infinitos
													}
												}
											}
										}
									}
								}
							}
						}
					}
				} else {
					if (campo.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PRODUCTO) == 0) {
						// Aqui deberia ser obtener por campo
						List<DetallePedidoVentaDTO> detalleFromDocument = detallePedidoVentaService
								.listar2Documento(campo.getDocumento());
						if (detalleFromDocument != null && !detalleFromDocument.isEmpty()) {
							relations = getRealationsRelatedWithList(referidas, relations);
							List<PropiedadDTO> propertiesWithRelationField = getPropertiesWithRelation(campo, referidas,
									relations);
							if (propertiesWithRelationField != null && !propertiesWithRelationField.isEmpty()) {
								for (int i = 0; i < detalleFromDocument.size(); i++) {
									DetallePedidoVentaDTO iRelation = detalleFromDocument.get(i);
									parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "I_" + codeReplace
											+ valueAuxToCode + "[" + String.valueOf(i + 1) + "]="
											+ SharedConstants.LINEA_MEDIA_DOBLE + "L_NUM" + SharedConstants.COMA_DOBLE
											+ String.valueOf(i + 1) + SharedConstants.LINEA_MEDIA_DOBLE + "L_VAL"
											+ SharedConstants.COMA_DOBLE + iRelation.getValorTotal().intValue();
									for (PropiedadDTO iProp : propertiesWithRelationField) {
										if (iProp.getMotivo() != null) {
											parameters = parameters + SharedConstants.LINEA_MEDIA_DOBLE + "L" + "_"
													+ iProp.getMotivo() + SharedConstants.COMA_DOBLE + documentService
															.consultaXId(iRelation.getProductoDocumento()).getNombre();
										}
										List<RelacionInternaDTO> relaciones = relations.get(iProp.getLlaveTabla());
										if (relaciones != null && !relaciones.isEmpty()) {
											// Todo esto practimanete lo copie de la funcion de arriba de referidos
											List<PedidoVentaCaracteristicaDTO> camposOpcionReferidos = new ArrayList<>();
											PedidoVentaCaracteristicaDTO aux = new PedidoVentaCaracteristicaDTO();
											aux.setValorOpcion(iRelation.getProductoDocumento());
											List<PedidoVentaCaracteristicaDTO> listAux = new ArrayList<PedidoVentaCaracteristicaDTO>();
											listAux.add(aux);
											//2025-02-09 Como hice que se guardar en formularios los detalles entonces vamos a buscar relaciones ene sos formularios
											if(iRelation.getDetalleId()!=null) {
												PedidoVentaCaracteristicaDTO auxForm = new PedidoVentaCaracteristicaDTO();
												auxForm.setValorOpcion(iRelation.getDetalleId());
												listAux.add(auxForm);
											}
											camposOpcionReferidos
													.addAll(campoService.listar2getApiCode(listAux, relaciones));
											List<PedidoVentaCaracteristicaDTO> camposReferidos = getFieldsFromOtherDocument(
													relaciones, camposOpcionReferidos);
											if (camposReferidos != null) {
												for (PedidoVentaCaracteristicaDTO iCampo : camposReferidos) {
													if (iCampo.getValorText() != null) {
														if (iCampo.getCampoDTO() == null)
															iCampo.setCampoDTO(
																	fieldService.consultaXId(iCampo.getCampo()));
														String codeReplaceList = iCampo.getCampoDTO().getCodigo();
														if (iCampo.getTransaccionRegistro() != null)
															codeReplaceList = codeReplaceList + "("
																	+ iCampo.getTransaccionRegistro() + ")";
														// +ConstantesGenerales.LINEA_MEDIA_DOBLE
														// +"GUIA"+ConstantesGenerales.COMA_DOBLE+"CT100"
														parameters = parameters + SharedConstants.LINEA_MEDIA_DOBLE
																+ "L" + "_" + codeReplaceList
																+ SharedConstants.COMA_DOBLE + formatToReplaceAll(
																		iCampo, iCampo.getTransaccionRegistro());
														// Coloque el service en null para evitar que se generen ciclos
														// infinitos
													}
												}
											}
										}
									}
								}
							}

						}
					}
				}
			}
		}
		///////
		return parameters;
	}

	private List<PropiedadDTO> getPropertiesWithRelation(PedidoVentaCaracteristicaDTO campo,
			List<PropiedadDTO> listOfProperties, Map<String, List<RelacionInternaDTO>> relations) {
		if (listOfProperties == null)
			return null;
		List<PropiedadDTO> resultOfProperties = null;
		for (PropiedadDTO iProp : listOfProperties) {
			List<RelacionInternaDTO> relaciones = relations.get(iProp.getLlaveTabla());
			if (relaciones != null && !relaciones.isEmpty()) {
				for (RelacionInternaDTO iRelation : relaciones) {
					if (iRelation.getCampo().compareTo(campo.getCampo()) == 0) {
						if (resultOfProperties == null)
							resultOfProperties = new ArrayList<>();
						resultOfProperties.add(iProp);
						break;
					}
				}
			}
		}
		return resultOfProperties;
	}

	private Map<String, List<RelacionInternaDTO>> getRealationsRelatedWithList(List<PropiedadDTO> referidas,
			Map<String, List<RelacionInternaDTO>> relations) throws ServerException {
		if (relations == null) {
			relations = new HashMap<String, List<RelacionInternaDTO>>();
			for (PropiedadDTO iProp : referidas) {
				relations.put(iProp.getLlaveTabla(), relacionService.relacionesPropiedad(iProp.getLlaveTabla()));
			}
		}
		return relations;
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
		List<PedidoVentaCaracteristicaDTO> camposIntermedios = null;
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
					// Necesito el id del campo para consultar los expedientes de multiples
					PedidoVentaCaracteristicaDTO fieldNew = iField.clone();
					fieldNew.setLlaveTabla(iField.getLlaveTabla());
					fieldsInternal.add(fieldNew);
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
							if (iInternal.getTransaccionRegistro() != null) {
								if (camposIntermedios == null)
									camposIntermedios = new ArrayList<>();
								camposIntermedios.add(iInternal);
							}
							break;
						}
					}
				}
			}
			camposEscogidos = new ArrayList<PedidoVentaCaracteristicaDTO>();
			camposEscogidos.addAll(fieldsInternal);
			List<PedidoVentaCaracteristicaDTO> mailInternal = getFieldsFromOtherDocument(relacionesSinRepetir,
					fieldsRelation);
			if (mailInternal != null)
				camposEscogidos.addAll(mailInternal);
			if (camposIntermedios != null)
				camposEscogidos.addAll(camposIntermedios);

		}
		return camposEscogidos;
	}

	/**
	 * 
	 * @param iCampo
	 * @return
	 * @throws ServerException 
	 */
	private String formatToReplaceAll(PedidoVentaCaracteristicaDTO iCampo, String auxiliarFormat) throws ServerException {
		if (iCampo == null || iCampo.getCampoDTO() == null || iCampo.getValorText() == null)
			return "";
		switch (iCampo.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.FECHA:
			if (auxiliarFormat == null)
				return iCampo.getValorText();
			iCampo.setValorFecha(getDateWithTransformations(iCampo.getValorFecha(), auxiliarFormat));
			if (auxiliarFormat.contains("("))
				auxiliarFormat = auxiliarFormat.substring(0, auxiliarFormat.indexOf("("));
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
			if (iCampo.getValorNumero() == null)
				return iCampo.getValorText();
			return new DecimalFormat("#.######").format(iCampo.getValorNumero().doubleValue());
		// Creo que la mejor solucion es mirar la propiedad de redondeo pero lo hare
		// despues
		case DocumentoPlantillaCaracteristicaDTO.PROCESO:
		case DocumentoPlantillaCaracteristicaDTO.TEXTO:
			return iCampo.getValorText();
		default:
			return iCampo.getValorText();
		}
	}

	public Date getDateWithTransformations(Date result, String texto) throws ServerException {
		if (!texto.contains("("))
			return result;
		// Tengo que uitar esto y dejar todo por milisegundos
		// Ejemplo E_FECHA_XXX[-15D]
		
		
		String formulaTime = texto.substring(texto.indexOf("(") + 1, texto.length() - 1);
		if (formulaTime.startsWith("P")) {
			try {
				Calendar fechaInicial = new GregorianCalendar();
				fechaInicial.setTime(result);
				String[] periodos = formulaTime.split("T");
				if (periodos[0].length() > 1) {
					Period pr = Period.parse(periodos[0]);
					if (pr.getYears() != 0) {
						fechaInicial.add(Calendar.YEAR, pr.getYears());
					}
					if (pr.getMonths() != 0) {
						fechaInicial.add(Calendar.MONTH, pr.getMonths());
					}
					if (pr.getDays() != 0) {
						fechaInicial.add(Calendar.DATE, pr.getDays());
					}
				}
				if (periodos.length > 1) {
					Duration lt = Duration.parse("PT" + periodos[1]);
					if (lt.toHoursPart() != 0) {
						fechaInicial.add(Calendar.HOUR, lt.toHoursPart());
					}

					if (lt.toMinutesPart() != 0) {
						fechaInicial.add(Calendar.MINUTE, (int) lt.toMinutesPart());
					}
					if (lt.toSecondsPart() != 0) {
						fechaInicial.add(Calendar.SECOND, (int) lt.toSecondsPart());
					}
				}
				return fechaInicial.getTime();
			} catch (DateTimeParseException e) {
				throw new ServerException("La fecha " + texto + " nose configura correctamente. e = " + e.getMessage());
			}
		} else {
			
			if (formulaTime.endsWith("D") )
				formulaTime = formulaTime.substring(0, formulaTime.length()-1);
			long timeToAdd = 0;
			try {
				timeToAdd = Long.parseLong(formulaTime.substring(1));
			} catch (Exception e) {
				timeToAdd = 365 * 10 * 24 * 60 * 60 * 1000; // Si hay error le sumo 10 years
			}
			if (formulaTime.contains("-"))
				timeToAdd = timeToAdd * -1; // Si es negativo
			if (texto.substring(texto.indexOf("(") + 1, texto.length() - 1).endsWith("D"))//Esto es para ese calculo de dias
				timeToAdd = timeToAdd * 24 * 60 * 60 * 1000;
			return new Date(result.getTime() + timeToAdd);

		}
	}
}
