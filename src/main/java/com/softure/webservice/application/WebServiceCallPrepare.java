package com.softure.webservice.application;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.java.services.SoftureUtil;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;
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
	
	public WebServiceEjecucionDTO call(WebServiceDTO service, PedidoVentaDTO document,
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
			if(campo.getExpedientes()!=null && !campo.getExpedientes().isEmpty()) {
				PedidoVentaDTO iElement = campo.getExpedientes().get(0);
				if(iElement!=null && iElement.getNombre()!=null) {
					parameters = parameters + ConstantesGenerales.PUNTO_COMA_DOBLE +  tipo + "_" + codeReplace
							+ ((iRelacion.getAuxiliar() != null) ? "(" + iRelacion.getAuxiliar() + ")" : "") + "_ID"
							+ ConstantesGenerales.IGUAL + iElement.getNombre();
				}	
			}
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
			return new DecimalFormat("#.######").format(iCampo.getValorNumero().doubleValue());
			//Creo que la mejor solucion es mirar la propiedad de redondeo pero lo hare despues
		case DocumentoPlantillaCaracteristicaDTO.PROCESO:
		case DocumentoPlantillaCaracteristicaDTO.TEXTO:
			return iCampo.getValorText();
		default:
			return iCampo.getValorText();
		}
	}

}
