package d3.report;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;

import d3.java.services.D3Utils;
import d3.report.application.ReporteBaseSvc;
import d3.report.domain.ReportDTO;
import d3.report.domain.ReporteBaseDTO;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ReporteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ReporteBaseSvc reporteBaseService;

	public ReporteServlet(@Lazy ReporteBaseSvc reportingSvc) {
		this.reporteBaseService = reportingSvc;
	}

	public void downloadFile(HttpServletResponse response, byte[] pInputStream, String fileName) {
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
			response.setHeader("filename", fileName);
			if (fileName.toLowerCase().contains(".pdf")) {
				response.setContentType("application/pdf");
			} else if (fileName.toLowerCase().contains(".xls")) {
				response.setContentType("application/vnd.ms-excel");
			} else if (fileName.toLowerCase().contains(".csv")) {
				response.setContentType("text/csv");
			} else if (fileName.toLowerCase().contains(".html")) {
				response.setContentType("text/html");
			} else if (fileName.toLowerCase().contains(".htm")) {
				response.setContentType("text/html");
			} else if (fileName.toLowerCase().contains(".doc")) {
				response.setContentType("application/msword");
			} else if (fileName.toLowerCase().contains(".config")) {
				response.setContentType("text/plain");
			} else if (fileName.toLowerCase().contains(".jrxml")) {
				response.setContentType("application/jrxml");
			} else if (fileName.toLowerCase().contains(".js")) {
				response.setContentType("text/javascript");
			} else if (fileName.toLowerCase().contains(".css")) {
				response.setContentType("text/css");
			} else if (fileName.toLowerCase().contains(".gif")) {
				response.setContentType("image/gif");
			} else if (fileName.toLowerCase().contains(".png")) {
				response.setContentType("image/png");
			} else if (fileName.toLowerCase().contains(".jpg")) {
				response.setContentType("image/jpg");
			} else if (fileName.toLowerCase().contains(".txt")) {
				response.setContentType("application/msword");
			}
			ServletOutputStream out = response.getOutputStream();
			out.write(pInputStream);
			/*
			 * byte[] buffer = new byte[1024]; while (pInputStream.read(buffer) > 0) {
			 * out.write(buffer); } out.flush(); pInputStream.close();
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			String nombreReporte = request.getParameter("nombre");
			if (nombreReporte == null)
				nombreReporte = request.getParameter("n");
			ReporteBaseDTO reportBD = reporteBaseService.validateReport(nombreReporte, request.getParameter("P_TOKEN"));
			// Esto debo cambiarlo despues con una validacion de permisos del usuario, por
			// el momento deje asi
			Map<String, Object> parametrosJasper = new HashMap<String, Object>();
			// seccion de parametros
			for (Enumeration<String> parametros = request.getParameterNames(); parametros.hasMoreElements();) {
				String parametro = (String) parametros.nextElement();
				Timestamp date = D3Utils.verificarFechaHora(request.getParameter(parametro));
				if (date == null) {
					String parametroUpper = parametro.toUpperCase();
					if (parametroUpper.compareTo("P_MULTIPLE") == 0) {
						parametrosJasper.put(parametroUpper, request.getParameter(parametro).split(";"));
					} else {
						parametrosJasper.put(parametroUpper, request.getParameter(parametro));
					}
				} else {
					parametrosJasper.put(parametro.toUpperCase(), date);
				}
			}
			String key = request.getParameter(ReporteBaseSvc.P_KEY);

			ReportDTO resultado = reporteBaseService.generarReporte(reportBD, key, parametrosJasper,
					request.getParameter("P_TOKEN"));
			if (resultado != null && resultado.getContent() != null) {
				// InputStream input = new ByteArrayInputStream(resultado.getContent());
				String tipoReporte = (String) parametrosJasper.get("P_JASPERTIPO");
				if (tipoReporte == null)
					tipoReporte = "pdf";// Corrige que los reportes se guarden como .null
				String name = resultado.getName();
				if (name == null) {
					name = reportBD.getNombre();
				}
				downloadFile(response, resultado.getContent(),
						name + "_" + D3Utils.formatDateMassiveFile(new Date()).replaceAll("\\\\", "_") + "."
								+ tipoReporte.toLowerCase());
			}
		} catch (Exception e) {
			PrintWriter out = response.getWriter();
			out.println("<result><operation state='false'>" + e.getMessage() + "</operation></result>");
			out.close();
		}
	}

}
