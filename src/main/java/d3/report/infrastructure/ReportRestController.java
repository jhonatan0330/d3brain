package d3.report.infrastructure;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import d3.report.application.ReporteBaseSvc;
import d3.report.domain.ReportDTO;
import d3.report.domain.ReporteBaseDTO;
import d3.shared.application.D3Utils;
import d3.shared.domain.ServerException;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ReportRestController {

	private final ReporteBaseSvc reporteBaseService;

	public ReportRestController(@Lazy ReporteBaseSvc reporteBaseService) {
		this.reporteBaseService = reporteBaseService;
	}

	@GetMapping("/generate")
	public ResponseEntity<byte[]> generate(@RequestParam Map<String, String> allParams,
			@RequestParam(name = "P_KEY", required = false) String key,
			@RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
		String nombre = allParams.get("nombre");
		if (nombre == null)
			nombre = allParams.get("n");
		if (nombre == null || nombre.isEmpty())
			throw new ServerException("Es obligatorio enviar el nombre del reporte");
		if (token == null)
			token = allParams.get("P_TOKEN");
		ReporteBaseDTO reportBD = reporteBaseService.validateReport(nombre, token);
		Map<String, Object> parametros = new HashMap<String, Object>();
		for (Map.Entry<String, String> entry : allParams.entrySet()) {
			String parametro = entry.getKey();
			if ("nombre".equalsIgnoreCase(parametro) || "n".equalsIgnoreCase(parametro))
				continue;
			Timestamp date = D3Utils.verificarFechaHora(entry.getValue());
			String parametroUpper = parametro.toUpperCase();
			if (date == null) {
				if (parametroUpper.compareTo("P_MULTIPLE") == 0)
					parametros.put(parametroUpper, entry.getValue().split(";"));
				else
					parametros.put(parametroUpper, entry.getValue());
			} else {
				parametros.put(parametroUpper, date);
			}
		}
		try {
			ReportDTO resultado = reporteBaseService.generarReporte(reportBD, key, parametros, token);
			if (resultado == null || resultado.getContent() == null)
				throw new ServerException("No se pudo generar el reporte");
			String tipoReporte = (String) parametros.get("P_JASPERTIPO");
			if (tipoReporte == null)
				tipoReporte = "pdf";
			String name = resultado.getName();
			if (name == null)
				name = reportBD.getNombre();
			String extension = tipoReporte.toLowerCase();
			String fileName = name + "_" + D3Utils.formatDateMassiveFile(new Date()).replaceAll("\\\\", "_") + "."
					+ extension;
			return ResponseEntity.ok().header("Pragma", "No-cache").header("Cache-Control", "no-cache")
					.header("Content-Disposition", "inline; filename=\"" + fileName + "\"").header("filename", fileName)
					.contentType(MediaType.parseMediaType(obtenerContentType(extension)))
					.body(resultado.getContent());
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
	}

	private String obtenerContentType(String extension) {
		switch (extension) {
		case "pdf":
			return "application/pdf";
		case "xls":
			return "application/vnd.ms-excel";
		case "csv":
			return "text/csv";
		case "html":
		case "htm":
			return "text/html";
		case "doc":
		case "txt":
			return "application/msword";
		case "jrxml":
			return "application/jrxml";
		case "js":
			return "text/javascript";
		case "css":
			return "text/css";
		case "gif":
			return "image/gif";
		case "png":
			return "image/png";
		case "jpg":
			return "image/jpg";
		default:
			return "application/octet-stream";
		}
	}

	@ExceptionHandler(ServerException.class)
	public ResponseEntity<Map<String, String>> manejarServerException(ServerException e) {
		Map<String, String> body = new HashMap<String, String>();
		body.put("error", e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, String>> manejarException(Exception e) {
		Map<String, String> body = new HashMap<String, String>();
		body.put("error", e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}

}