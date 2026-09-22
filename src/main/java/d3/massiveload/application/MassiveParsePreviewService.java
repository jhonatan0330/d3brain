package d3.massiveload.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import d3.document.application.field.Propiedades;
import d3.document.domain.DocumentMessage;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.massiveload.domain.MassiveParseLineDTO;
import d3.massiveload.domain.MassiveParseResponse;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.TemplateDTO;
import d3.shared.application.D3Utils;
import d3.shared.domain.ServerException;

@Service
public class MassiveParsePreviewService {

	private static final int MAX_DOCUMENTS = 20000;
	private static final String ESTADO_OK = "OK";
	private static final String ESTADO_FAILED = "FAILED";
	private static final long FECHA_SERIAL_DIAS_OFFSET = 25568;
	private static final double FECHA_SERIAL_FRACCION = 0.791;

	private final MassiveLoadOrchestratorService orchestratorService;
	private final MassiveFileParserService parserService;
	private final MassiveDocumentBuilderService builderService;

	public MassiveParsePreviewService(@Lazy MassiveLoadOrchestratorService orchestratorService,
			@Lazy MassiveFileParserService parserService, @Lazy MassiveDocumentBuilderService builderService) {
		this.orchestratorService = orchestratorService;
		this.parserService = parserService;
		this.builderService = builderService;
	}

	public MassiveParseResponse parseFile(MultipartFile file, String templateId) throws ServerException {
		TemplateDTO plantilla = orchestratorService.obtenerPlantilla(templateId);
		String fileName = (file.getOriginalFilename() == null) ? "" : file.getOriginalFilename().toLowerCase();
		List<Map<String, String>> rows;
		if (fileName.endsWith(".xml")) {
			rows = parserService.parse(file, MassiveFileParserService.formatStringXML(plantilla.getCodigo()));
		} else {
			rows = parserService.parse(file);
		}
		if (rows == null || rows.isEmpty()) {
			throw new ServerException("No se generaron registros a partir del archivo");
		}
		if (rows.size() > MAX_DOCUMENTS) {
			throw new ServerException("El maximo de documentos a cargar son " + MAX_DOCUMENTS);
		}
		MassiveParseResponse response = new MassiveParseResponse();
		response.setCamposSinValidar(calcularCamposSinValidar(rows, plantilla));
		int order = 0;
		for (Map<String, String> row : rows) {
			order++;
			PedidoVentaDTO pedido = builderService.build(row, plantilla, true);
			pedido.setImagen(plantilla.getImagen());
			pedido.setTextoFiltro(String.valueOf(order));
			MassiveParseLineDTO line = new MassiveParseLineDTO();
			line.setOrderNumber(order);
			line.setUpdateId(pedido.getNombre());
			line.setStatus(ESTADO_OK);
			List<DocumentMessage> mensajes = normalizarCampos(pedido);
			hacerLegible(pedido);
			if (!mensajes.isEmpty()) {
				line.setStatus(ESTADO_FAILED);
				line.setMessages(mensajes);
			}
			line.setDocument(pedido);
			response.getLines().add(line);
		}
		return response;
	}

	private List<DocumentMessage> normalizarCampos(PedidoVentaDTO pedido) {
		List<DocumentMessage> mensajes = new ArrayList<>();
		if (pedido.getCaracteristicas() == null)
			return mensajes;
		for (PedidoVentaCaracteristicaDTO campo : pedido.getCaracteristicas()) {
			try {
				prepararCampo(campo);
			} catch (ServerException e) {
				DocumentMessage msg = new DocumentMessage();
				msg.setMessage(e.getMessage());
				mensajes.add(msg);
			}
		}
		return mensajes;
	}

	private void hacerLegible(PedidoVentaDTO pedido) {
		if (pedido.getCaracteristicas() == null)
			return;
		for (PedidoVentaCaracteristicaDTO campo : pedido.getCaracteristicas()) {
			campo.setPrincipal(null);
			campo.setCampoDTO(null);
		}
	}

	private void prepararCampo(PedidoVentaCaracteristicaDTO campo) throws ServerException {
		DocumentoPlantillaCaracteristicaDTO base = campo.getCampoDTO();
		if (base == null)
			return;
		switch (base.getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.FECHA:
			prepararFecha(campo, base);
			break;
		case DocumentoPlantillaCaracteristicaDTO.NUMERO:
			if (campo.getValorText() != null && !campo.getValorText().isEmpty()) {
				try {
					campo.setValorNumero(new BigDecimal(campo.getValorText()));
				} catch (NumberFormatException e) {
					throw new ServerException(
							"El valor " + campo.getValorText() + " del campo " + base.getNombre() + " no es un numero");
				}
			}
			break;
		case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION:
			campo.setValorOpcion(campo.getValorText());
			break;
		case DocumentoPlantillaCaracteristicaDTO.ARCHIVO:
			campo.setValorOpcion(campo.getValorText());
			campo.setValorText("SIN CARGAR");
			break;
		case DocumentoPlantillaCaracteristicaDTO.PROCESO:
			prepararProceso(campo, base);
			break;
		default:
			break;
		}
	}

	private void prepararFecha(PedidoVentaCaracteristicaDTO campo, DocumentoPlantillaCaracteristicaDTO base)
			throws ServerException {
		String valor = campo.getValorText();
		if (valor == null || valor.isEmpty())
			return;
		boolean conHora = Propiedades.obtenerParametro(base, Propiedades.FECHA_CON_HORA) != null;
		Date fecha;
		if (esNumero(valor)) {
			double serial = Double.parseDouble(valor);
			long millis = (long) ((serial - FECHA_SERIAL_DIAS_OFFSET - FECHA_SERIAL_FRACCION) * 86400 * 1000);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(millis);
			cal.add(Calendar.HOUR_OF_DAY, 5);
			cal.set(Calendar.SECOND, 0);
			if (!conHora) {
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.HOUR_OF_DAY, 0);
			}
			fecha = cal.getTime();
			campo.setValorText(conHora ? D3Utils.formatDateTime(fecha) : D3Utils.formatDate(fecha));
		} else {
			fecha = D3Utils.formatTimeDate(valor);
			if (fecha == null) {
				String mensaje = conHora
						? "El valor fecha no esta con el formato correcto utiliza el formato año/Mes/dia hora:minuto como el siguiente ejemplo 2023/04/26 23:59. La fecha actualmente tiene este formato "
								+ valor
						: "El valor fecha no esta con el formato correcto utiliza el formato año/Mes/dia como el siguiente ejemplo 2023/04/26. La fecha actualmente tiene este formato "
								+ valor;
				throw new ServerException(mensaje);
			}
			if (!conHora) {
				Calendar trunc = Calendar.getInstance();
				trunc.setTime(fecha);
				trunc.set(Calendar.HOUR_OF_DAY, 0);
				trunc.set(Calendar.MINUTE, 0);
				trunc.set(Calendar.SECOND, 0);
				trunc.set(Calendar.MILLISECOND, 0);
				fecha = trunc.getTime();
			}
			String textoNormalizado = conHora ? D3Utils.formatDateTime(fecha)
					: D3Utils.formatDate(fecha);
			campo.setValorText(textoNormalizado);
		}
		if (fecha != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(fecha);
			cal.add(Calendar.HOUR_OF_DAY, 5);
			campo.setValorFecha(cal.getTime());
		}
	}

	private boolean esNumero(String valor) {
		if (valor == null || valor.isEmpty())
			return false;
		try {
			Double.parseDouble(valor);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}


	private void prepararProceso(PedidoVentaCaracteristicaDTO campo, DocumentoPlantillaCaracteristicaDTO base)
			throws ServerException {
		if (Propiedades.obtenerParametro(base, Propiedades.CAMPO_HEREDADO_1) != null)
			return;
		if (Propiedades.obtenerParametro(base, Propiedades.MULTIPLE) != null) {
			if (campo.getValorText() != null && !campo.getValorText().isEmpty()) {
				List<PedidoVentaDTO> expedientes = new ArrayList<>();
				PedidoVentaDTO multiple = new PedidoVentaDTO();
				multiple.setNombre(campo.getValorText());
				expedientes.add(multiple);
				campo.setExpedientes(expedientes);
			}
			return;
		}
		if (Propiedades.obtenerVariosParametro(base, Propiedades.PLANTILLA_AUXILIAR) == null)
			throw new ServerException("El campo " + base.getNombre()
					+ " no tiene una fuente de datos en donde pueda buscar el numero del documento.");
	}

	private List<String> calcularCamposSinValidar(List<Map<String, String>> rows, TemplateDTO plantilla) {
		Map<String, Boolean> encabezados = new LinkedHashMap<>();
		for (Map<String, String> row : rows) {
			for (String header : row.keySet()) {
				encabezados.put(header, true);
			}
		}
		if (plantilla.getCaracteristicas() != null) {
			for (DocumentoPlantillaCaracteristicaDTO campo : plantilla.getCaracteristicas()) {
				encabezados.remove(MassiveFileParserService.formatStringXML(campo.getNombre()));
			}
		}
		List<String> sinValidar = new ArrayList<>();
		for (String key : encabezados.keySet()) {
			if (!key.endsWith("_NUMID") && !key.startsWith("UPDATE_"))
				sinValidar.add(key);
		}
		return sinValidar;
	}
}