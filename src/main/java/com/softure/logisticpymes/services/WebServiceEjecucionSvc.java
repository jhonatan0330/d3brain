package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.dto.WebServiceDTO;
import com.softure.logisticpymes.services.adapter.Propiedades;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.WebServiceEjecucionDTO;
import com.softure.logisticpymes.dto.filter.WebServiceEjecucionFilterDTO;
import com.softure.logisticpymes.persistence.WebServiceEjecucionMapper;

@Service("webServiceEjecucionService")
public class WebServiceEjecucionSvc extends BasicSvc<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO> {
	
	@Autowired
	private WebServiceEjecucionMapper webServiceEjecucionMapper;
	
	// BEGIN region servicesWebServiceEjecucion
	@Autowired private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired private WebServiceSvc webServiceSvc;
	@Autowired private PedidoVentaSvc pedidoService;
	@Autowired private PropiedadSvc propiedadesSvc;
	@Autowired private RelacionInternaSvc relacionService;
	@Autowired private UploadSvc uploadService;
	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	// END region servicesWebServiceEjecucion

	@Override
	public WebServiceEjecucionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. WebServiceEjecucion");
		WebServiceEjecucionFilterDTO dto = new WebServiceEjecucionFilterDTO();
		dto.setLlaveTabla(llave);
		return webServiceEjecucionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = webServiceEjecucionMapper;
	}
	
	@Override
	public WebServiceEjecucionDTO activar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_activar
		return super.activar(dto, token);
		// END WebServiceEjecucion_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO actualizar( WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_actualizar
		return super.actualizar(dto, token);
		// END WebServiceEjecucion_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO inactivar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_inactivar
		return super.inactivar(dto, token);
		// END WebServiceEjecucion_inactivar
	}
	
	@Override
	public WebServiceEjecucionDTO consultaUnica(WebServiceEjecucionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(WebServiceEjecucionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<WebServiceEjecucionDTO> listarConsulta(WebServiceEjecucionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public WebServiceEjecucionDTO guardar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_guardar
		return super.guardar(dto, token);
		// END WebServiceEjecucion_guardar
	}

// BEGIN region aditionalMethods
	public WebServiceEjecucionDTO ejecutar(String serviceId, PedidoVentaDTO document, PedidoVentaDTO modificador, String token)
			throws ServerException {
		WebServiceDTO service = webServiceSvc.consultaXId(serviceId);
		if (service == null)throw new ServerException("El id del servicio no se encuentra en la BD." + serviceId);
		System.out.format("\n\n[%s] Procesando API (%s)", document.getNombre(), service.getNombre());
		service.setPropiedades(propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE, serviceId, null, null));
		WebServiceEjecucionDTO callWS = new WebServiceEjecucionDTO();
		callWS.setServicio(service.getLlaveTabla());
		callWS.setFecha(new Date());
		String template = crearSalida(service, document, modificador);
		callWS.setEntrada(uploadService.uploadFile(template.getBytes(), "Entrada.txt"));
		callWS.setDocumento(document.getLlaveTabla());
		callWS.setUsuario(getUserFlex(token));
		String responseApi = null;
		try {
			responseApi = callApi(service, callWS, template);
		} catch (Exception e) {
			responseApi = e.getMessage();
			callWS.setError(e.getMessage());
		}
		callWS.setSalida(uploadService.uploadFile(responseApi.getBytes(), "Salida.txt"));
		callWS = save(callWS);
		if (callWS.getError() == null) {
			generateDocuments(service, responseApi, document, token);
		}
		System.out.format("\n\n[%s] Finalizando API (%s)", document.getNombre(), service.getNombre());
		return callWS;
	}

	private String crearSalida(WebServiceDTO service, PedidoVentaDTO document, PedidoVentaDTO modificador) throws ServerException {
		String template = service.getTemplate();
		if (service.getPropiedades() != null && !service.getPropiedades().isEmpty()) {
			// Directas
			List<PropiedadDTO> directas = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_DIRECT);
			if (directas != null && !directas.isEmpty()) {
				for (PropiedadDTO iProp : directas) {
					List<RelacionInternaDTO> relaciones = relacionService.relacionesPropiedad(iProp.getLlaveTabla());
					if (relaciones != null && !relaciones.isEmpty()) {
						List<PedidoVentaCaracteristicaDTO> camposOpcionales = null;
						if(document.getCaracteristicas()==null) {
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
								PedidoVentaCaracteristicaDTO campo = pedidoService
										.obtenerValor(camposOpcionales, iRelacion.getCampo());	
								if (campo != null)
									if (campo.getCampoDTO()==null) campo.setCampoDTO(fieldService.consultaXId(campo.getCampo()));
									template = template.replaceAll(
											"\\{\\{D_" + campo.getCampoDTO().getCodigo() + "\\}\\}",
											campo.getValorText());
							}
						}
					}
				}
			}
			// Especiales
			List<PropiedadDTO> especiales = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_ESPECIAL);
			if (especiales != null && !especiales.isEmpty()) {
				for (PropiedadDTO iProp : especiales) {
					if(iProp.getTexto()==null) throw new ServerException("Es necesario colocar texto en la propiedad de codigo especial " + iProp.getValor());
					if(iProp.getTexto().startsWith("E_FECHA_")) {
						template = template.replaceAll("\\{\\{" + iProp.getTexto() + "\\}\\}", SoftureUtil.formatDatePattern(new Date(),iProp.getValor()));
					}else {
						template = template.replaceAll("\\{\\{" + iProp.getTexto() + "\\}\\}", iProp.getValor());
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
						if(document.getCaracteristicas()==null) {
							PedidoVentaCaracteristicaDTO aux = new PedidoVentaCaracteristicaDTO();
							aux.setValorOpcion(document.getLlaveTabla());
							List<PedidoVentaCaracteristicaDTO> listAux = new ArrayList<PedidoVentaCaracteristicaDTO>();
							listAux.add(aux);
							camposOpcionales = campoService.listar2getApiCode(listAux, relaciones);
						}else {
							camposOpcionales = document.getCaracteristicas();
						}
						List<PedidoVentaCaracteristicaDTO> camposReferidos = consultarCamposReferidos(relaciones, camposOpcionales);
						for (PedidoVentaCaracteristicaDTO iCampo : camposReferidos) {
							if (iCampo.getCampoDTO()==null) iCampo.setCampoDTO(fieldService.consultaXId(iCampo.getCampo()));
							template = template.replaceAll("\\{\\{R_" + iCampo.getCampoDTO().getCodigo() + "\\}\\}",iCampo.getValorText());
						}
					}
				}
			}
			if(modificador !=null) {
				// modificador
				List<PropiedadDTO> modificadoras = Propiedades.obtenerVariosParametro(service, Propiedades.API_CODE_MODIFICADOR);
				if (modificadoras != null && !modificadoras.isEmpty()) {
					for (PropiedadDTO iProp : modificadoras) {
						List<RelacionInternaDTO> rModificadoras = relacionService.relacionesPropiedad(iProp.getLlaveTabla());
						if (rModificadoras != null && !rModificadoras.isEmpty()) {
							List<PedidoVentaCaracteristicaDTO> camposOpcionales = null;
							if(modificador.getCaracteristicas()==null) {
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
									PedidoVentaCaracteristicaDTO campo = pedidoService
											.obtenerValor(camposOpcionales, iRelacion.getCampo());	
									if (campo != null)
										if (campo.getCampoDTO()==null) campo.setCampoDTO(fieldService.consultaXId(campo.getCampo()));
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
		template = template.replaceAll("\\{\\{[A-Za-z0-9_]*\\}\\}", "");
		return template;
	}
	
	private List<PedidoVentaCaracteristicaDTO> consultarCamposReferidos(List<RelacionInternaDTO> relaciones, List<PedidoVentaCaracteristicaDTO> fields) throws ServerException {
		if(relaciones==null || relaciones.isEmpty() || fields == null || fields.isEmpty()) return null; 
		List<PedidoVentaCaracteristicaDTO> camposEscogidos = null;
		List<PedidoVentaCaracteristicaDTO> fieldsInternal = null; // Campos que van cumpliendo con lo que queremos
		List<RelacionInternaDTO> relacionesValidadas = new ArrayList<RelacionInternaDTO>();

		for (RelacionInternaDTO iRelacion : relaciones) {
			for (PedidoVentaCaracteristicaDTO iField : fields) {
				if(iRelacion.getCampo().compareTo(iField.getCampo())==0) {
					relacionesValidadas.add(iRelacion); // Esta relacion despues se vaa borrar por eso la adiciono
					if(fieldsInternal==null) fieldsInternal = new ArrayList<PedidoVentaCaracteristicaDTO>();
					fieldsInternal.add(iField);
				}
			}
		}
		if(fieldsInternal!=null) {
			//Esto me toco hacerlo porque se descuadranban los array al remove la relacion
			List<RelacionInternaDTO> relacionesSinRepetir = new ArrayList<RelacionInternaDTO>();
			relacionesSinRepetir.addAll(relaciones);
			for (RelacionInternaDTO iRelacion : relacionesValidadas) {
				relacionesSinRepetir.remove(iRelacion);
			}
			List<PedidoVentaCaracteristicaDTO> fieldsRelation = campoService.listar2getApiCode(fieldsInternal, relacionesSinRepetir);
			if(fieldsRelation!=null) {
				// Retiro los campos
				for (PedidoVentaCaracteristicaDTO iFRelation : fieldsRelation) {
					for (PedidoVentaCaracteristicaDTO iInternal : fieldsInternal) {
						if(iInternal.getValorOpcion()!=null && iInternal.getValorOpcion().compareTo(iFRelation.getDocumento())==0) {
							fieldsInternal.remove(iInternal);
							break;
						}
					}
				}
			}
			camposEscogidos = new ArrayList<PedidoVentaCaracteristicaDTO>();
			camposEscogidos.addAll(fieldsInternal);				
			List<PedidoVentaCaracteristicaDTO> mailInternal = consultarCamposReferidos(relacionesSinRepetir, fieldsRelation);
			if(mailInternal!=null) {
				camposEscogidos.addAll(mailInternal);
			}
		}
		return camposEscogidos;
	}

	private String callApi(WebServiceDTO service, WebServiceEjecucionDTO callWS, String template)
			throws ServerException {
		URL url;
		try {
			url = new URL(service.getServidorNombre());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);

			if (service.getPropiedades() != null && !service.getPropiedades().isEmpty()) {
				for (PropiedadDTO iProp : service.getPropiedades()) {
					if (iProp.getKey().compareTo(Propiedades.API_HEADER) == 0) {
						con.setRequestProperty(iProp.getValor(), iProp.getMotivo());
					}
				}
			}
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			con.connect();

			// Send request
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(template);
			wr.flush();
			wr.close();

			System.out.format("\n[] Procesando API status (%s), getErrorStream (%s)", con.getResponseCode());
			
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
		return pedidoService.guardar(nuevo, token);
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
	
	
// END region aditionalMethods

}