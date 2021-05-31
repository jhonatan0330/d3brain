package com.softure.logisticpymes.services;

// BEGIN region interImport
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
import com.softure.logisticpymes.dto.filter.WebServiceEjecucionFilterDTO;
import com.softure.logisticpymes.persistence.WebServiceEjecucionMapper;
import com.softure.logisticpymes.services.adapter.Propiedades;
// END region interImport

@Service("webServiceEjecucionService")
public class WebServiceEjecucionSvc extends BasicSvc<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO> {

	@Autowired
	private WebServiceEjecucionMapper webServiceEjecucionMapper;

	// BEGIN region servicesWebServiceEjecucion
	@Autowired private DocumentoPlantillaCaracteristicaSvc campoService;
	@Autowired private WebServiceSvc webServiceSvc;
	@Autowired private PedidoVentaSvc pedidoService;
	@Autowired private PropiedadSvc propiedadesSvc;
	@Autowired private RelacionInternaSvc relacionService;
	@Autowired private UploadSvc uploadService;
	// END region servicesWebServiceEjecucion

	@Override
	public WebServiceEjecucionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. WebServiceEjecucion");
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
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public WebServiceEjecucionDTO actualizar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_actualizar
		return super.actualizar(dto, token);
		// END WebServiceEjecucion_actualizar
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
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
	public List<WebServiceEjecucionDTO> listarConsulta(WebServiceEjecucionFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public WebServiceEjecucionDTO guardar(WebServiceEjecucionDTO dto, String token) throws ServerException {
		// BEGIN WebServiceEjecucion_guardar
		return super.guardar(dto, token);
		// END WebServiceEjecucion_guardar
	}

// BEGIN region aditionalMethods
	public WebServiceEjecucionDTO ejecutar(String serviceId, PedidoVentaDTO document, String token)
			throws ServerException {
		WebServiceDTO service = webServiceSvc.consultaXId(serviceId);
		if (service == null) throw new ServerException("El id del servicio no se encuentra en la BD." + serviceId);
		service.setPropiedades(propiedadesSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.API_SERVICE, serviceId, null, null));
		WebServiceEjecucionDTO callWS = new WebServiceEjecucionDTO();
		callWS.setServicio(service.getLlaveTabla());
		callWS.setFecha(new Date());
		callWS.setEntrada(uploadService.uploadFile(service.getTemplate().getBytes(), token));
		callWS.setDocumento(document.getLlaveTabla());
		String responseApi = callApi(service, callWS);
		callWS.setSalida(uploadService.uploadFile(responseApi.getBytes(), token));
		callWS = save(callWS);
		generateDocuments(service, responseApi, document, token);
		return callWS;
	}

	public String callApi(WebServiceDTO service, WebServiceEjecucionDTO callWS) throws ServerException {
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
			/*
			 * OutputStream os = httpCon.getOutputStream(); OutputStreamWriter osw = new
			 * OutputStreamWriter(os, "UTF-8"); osw.write("Just Some Text"); osw.flush();
			 * osw.close(); os.close(); //don't forget to close the OutputStream
			 * httpCon.connect();
			 * 
			 * //read the inputstream and print it String result; BufferedInputStream bis =
			 * new BufferedInputStream(httpCon.getInputStream()); ByteArrayOutputStream buf
			 * = new ByteArrayOutputStream(); int result2 = bis.read(); while(result2 != -1)
			 * { buf.write((byte) result2); result2 = bis.read(); } result = buf.toString();
			 * System.out.println(result);
			 */
			// con.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			// con.setRequestProperty("SOAPAction", "http://sap.com/xi/WebService/soap1.1");
			// con.setRequestProperty("Authorization", "Basic
			// UElEVFJBTlNQTzpCIyQlLio5Iw==");

			// con.setConnectTimeout(5000);
			// con.setReadTimeout(5000);
			try (OutputStream os = con.getOutputStream()) {
				byte[] input = service.getTemplate().getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
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

	public void generateDocuments(WebServiceDTO service, String responseWS, PedidoVentaDTO document, String token)
			throws ServerException {
		List<PropiedadDTO> newTemplates = Propiedades.obtenerVariosParametro(service, Propiedades.API_NEW_DOCUMENT);
		List<PropiedadDTO> secondaryTemplates = Propiedades.obtenerVariosParametro(service, Propiedades.API_SECONDARY_DOCUMENT);
		if (newTemplates != null && !newTemplates.isEmpty()) {
			HashMap<String, List<DocumentoPlantillaCaracteristicaDTO>> hmapTemplate = new HashMap<String, List<DocumentoPlantillaCaracteristicaDTO>>();
			HashMap<String, List<RelacionInternaDTO>> hmapRelaciones = new HashMap<String, List<RelacionInternaDTO>>();
			for (PropiedadDTO iProp : newTemplates) {
				final Matcher matcher = Pattern.compile(iProp.getMotivo()).matcher(responseWS);
				while (matcher.find()) {
					PedidoVentaDTO nuevo = createNewDocument(hmapTemplate, hmapRelaciones, iProp.getValor(), iProp.getLlaveTabla(), matcher.group(1),
							document, token);
					// Envio a guardar los documentos secundarios
					if (secondaryTemplates != null && !secondaryTemplates.isEmpty()) {
						for (PropiedadDTO iProp2 : secondaryTemplates) {
							final Matcher matcherSecond = Pattern.compile(iProp2.getMotivo()).matcher(matcher.group(1));
							while (matcherSecond.find()) {
								createNewDocument(hmapTemplate, hmapRelaciones, iProp2.getValor(), iProp2.getLlaveTabla(), matcherSecond.group(1),
										nuevo, token);
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
			camposPlantilla = campoService.listarCamposPlantillaConComplementos(templateId, token);
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
	public PedidoVentaCaracteristicaDTO createField(DocumentoPlantillaCaracteristicaDTO campo,
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
	/*
	 * public static String getParamsString(Map<String, String> params) throws
	 * UnsupportedEncodingException{ StringBuilder result = new StringBuilder();
	 * 
	 * for (Map.Entry<String, String> entry : params.entrySet()) {
	 * result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
	 * result.append("="); result.append(URLEncoder.encode(entry.getValue(),
	 * "UTF-8")); result.append("&"); }
	 * 
	 * String resultString = result.toString(); return resultString.length() > 0 ?
	 * resultString.substring(0, resultString.length() - 1) : resultString; }
	 */
// END region aditionalMethods

}