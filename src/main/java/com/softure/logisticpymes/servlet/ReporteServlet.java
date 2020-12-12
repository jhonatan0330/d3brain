package com.softure.logisticpymes.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import com.softure.java.services.ReportesUtil;
import com.softure.logisticpymes.dto.ReporteBaseDTO;
import com.softure.logisticpymes.services.ReporteBaseSvc;

public class ReporteServlet extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@Autowired private ReporteBaseSvc reporteBaseService;

	public void downloadFile(HttpServletResponse response, InputStream pInputStream, String fileName) {
		try {
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.setHeader("Content-Disposition", "inline; filename=\"" + fileName + "\"");
			response.setHeader("filename", fileName);
			if (fileName.toLowerCase().contains(".pdf")) {
				response.setContentType("application/pdf");
			}			
			else if (fileName.toLowerCase().contains(".xls")) {
				response.setContentType("application/vnd.ms-excel");
			}
			else if (fileName.toLowerCase().contains(".doc")) {
				response.setContentType("application/msword");
			}
			else if (fileName.toLowerCase().contains(".config")) {
				response.setContentType("text/plain");
			}
			else if(fileName.toLowerCase().contains(".jrxml")){
				response.setContentType("application/jrxml");
			}
			else if (fileName.toLowerCase().contains(".js")){
				response.setContentType("text/javascript");
			}
			else if (fileName.toLowerCase().contains(".htm")){
				response.setContentType("text/html");
			}
			else if (fileName.toLowerCase().contains(".css")){
				response.setContentType("text/css");
			}
			else if (fileName.toLowerCase().contains(".gif")){
				response.setContentType("image/gif");
			}
			else if (fileName.toLowerCase().contains(".png")){
				response.setContentType("image/png");
			}
			else if (fileName.toLowerCase().contains(".jpg")){
				response.setContentType("image/jpg");
			}
			else if (fileName.toLowerCase().contains(".txt")){
				response.setContentType("application/msword");
			}
			ServletOutputStream out = response.getOutputStream();
			byte[] buffer = new byte[1024];
			while (pInputStream.read(buffer) > 0) {
				out.write(buffer);
			}
			out.flush();
			pInputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {		
		try {
			//Esto debo cambiarlo despues con una validacion de permisos del usuario, por el momento deje asi
			Map<String, Object> parametrosJasper = new HashMap<String, Object>(); 
			//seccion de parametros
			for (Enumeration<String> parametros = request.getParameterNames(); parametros.hasMoreElements();) {
				String parametro = (String) parametros.nextElement();
				Timestamp date = ReportesUtil.verificarFechaHora(request.getParameter(parametro));
				if (date == null) {
					String parametroUpper = parametro.toUpperCase();
					if(parametroUpper.compareTo("P_MULTIPLE")==0) {
						parametrosJasper.put(parametroUpper, request.getParameter(parametro).split(";"));
					}else {
						parametrosJasper.put(parametroUpper, request.getParameter(parametro));
					}
				} else {
					parametrosJasper.put(parametro.toUpperCase(), date);
				}
			}
			
			String nombreReporte = request.getParameter("nombre");
			String key = request.getParameter(ReporteBaseSvc.P_KEY);
			byte[] resultado = reporteBaseService.generarReporte(nombreReporte, key, parametrosJasper, null);
			if(resultado!=null){
				InputStream input = new ByteArrayInputStream(resultado);
				String tipoReporte = (String) parametrosJasper.get("P_JASPERTIPO");
				if(tipoReporte==null) tipoReporte = "pdf";//Corrige que los reportes se guarden como .null
				ReporteBaseDTO base = reporteBaseService.consultaXId(nombreReporte);
				downloadFile(response, input, base.getNombre() +"_(" + DateFormat.getInstance().format(new Date()) + ")." + tipoReporte);
			}
		} catch (Exception e) {
			PrintWriter out=response.getWriter();
			out.println("<result><operation state='false'>" +e.getMessage() +"</operation></result>");
			out.close();
		}
	}

}


