package com.softure.report.application;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;

public class ReportesUtil {

	private static final String P_SUBREPORT = "P_SUBREPORT_";
	private static final String REPORTE_ENCABEZADO = "REPORTE_ENCABEZADO";
	private static final String REPORTE_ENCABEZADO_EXCEL = "REPORTE_ENCABEZADO_EXCEL";
	private static final String REPORTE_PIE_PAGINA = "REPORTE_PIE_PAGINA";

	public static byte[] exportarReporteExcel(String reportejrxml, Map<String, Object> parametrosReporte, Connection conexion) throws Exception {
		try {
			if(parametrosReporte.containsKey(REPORTE_ENCABEZADO_EXCEL)) {
				JasperReport encabezado = JasperCompileManager.compileReport(new ByteArrayInputStream(parametrosReporte.get(REPORTE_ENCABEZADO_EXCEL).toString().getBytes("utf-8")));
				parametrosReporte.put(REPORTE_ENCABEZADO_EXCEL, encabezado);
				reportejrxml = replaceHeader(reportejrxml, REPORTE_ENCABEZADO_EXCEL);
			}
			for (Map.Entry<String, Object> entry: parametrosReporte.entrySet()) {
				if(entry.getKey().startsWith(P_SUBREPORT)) {
					JasperReport jasperSubReport = JasperCompileManager.compileReport(new ByteArrayInputStream(entry.getValue().toString().getBytes("utf-8")));
					parametrosReporte.put(entry.getKey(), jasperSubReport);
					reportejrxml = replaceReport(reportejrxml,jasperSubReport.getName(), entry.getKey());
				}
			}
			
			JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(reportejrxml.getBytes("utf-8")));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametrosReporte, conexion);
			ByteArrayOutputStream vByteOutputStream = new ByteArrayOutputStream();
			JRXlsExporter vXlsExporter = new JRXlsExporter();
			vXlsExporter.setExporterInput(new SimpleExporterInput( jasperPrint));
			vXlsExporter.setExporterOutput(new  SimpleOutputStreamExporterOutput(vByteOutputStream));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setCollapseRowSpan(true);
			configuration.setDetectCellType(true);
			configuration.setIgnoreGraphics(false);
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			configuration.setWhitePageBackground(false);
			configuration.setWrapText(true);
			
			vXlsExporter.setConfiguration(configuration);
			vXlsExporter.exportReport();
			vByteOutputStream.close();	
			return vByteOutputStream.toByteArray();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	private static String replaceReport(String report, String nameSR, String nameParameter) {
		report = report.replace("<queryString", "<parameter name=\""+ nameParameter +"\" class=\"net.sf.jasperreports.engine.JasperReport\"/>\r\n	<queryString");
		report = report.replace("<subreportExpression><![CDATA[\"" + nameSR + ".jasper\"]]></subreportExpression>", "<subreportExpression class=\"net.sf.jasperreports.engine.JasperReport\" ><![CDATA[$P{" + nameParameter + "}]]></subreportExpression>");
		return report;
	}
	
	private static String replaceHeader(String report, String nameSubreport) throws Exception {
		report = report.replace("<parameter name=\"P_KEY\" class=\"java.lang.String\"/>", "");
		report = report.replace("<queryString", "<parameter name=\"NOMBRE\" class=\"java.lang.String\"/>\r\n" + 
				"	<parameter name=\"P_KEY\" class=\"java.lang.String\"/>\r\n" + 
				"   <parameter name=\""+nameSubreport+"\" class=\"net.sf.jasperreports.engine.JasperReport\"/>\r\n "+ 
				"   <queryString");
		int inicio = report.indexOf("<pageHeader>");
		if(inicio<0) throw new Exception("No se encuentra el inicio del tag <pageHeader> ");
		int fin = report.substring(inicio+1).indexOf("</pageHeader>");
		if(fin<0) throw new Exception("No se encuentra el fin del tag </pageHeader> ");
		report = report.replace(report.substring(inicio, inicio + fin), "<pageHeader>\r\n"+ 
				"		<band height=\"20\" splitType=\"Stretch\">\r\n" + 
				"			<subreport>\r\n" + 
				"				<reportElement x=\"0\" y=\"0\" width=\"572\" height=\"20\" uuid=\"05fdcc2b-1e35-49dc-8aea-6e9edde5f626\"/>\r\n" + 
				"				<subreportParameter name=\"P_KEY\">\r\n" + 
				"					<subreportParameterExpression><![CDATA[$P{P_KEY}]]></subreportParameterExpression>\r\n" + 
				"				</subreportParameter>\r\n" + 
				"				<subreportParameter name=\"P_REPORTE\">\r\n" + 
				"					<subreportParameterExpression><![CDATA[$P{NOMBRE}]]></subreportParameterExpression>\r\n" + 
				"				</subreportParameter>\r\n" + 
				"				<subreportParameter name=\"P_NUMBER_PAGE\">\r\n" + 
				"					<subreportParameterExpression><![CDATA[$V{PAGE_NUMBER}]]></subreportParameterExpression>\r\n" + 
				"				</subreportParameter>\r\n" + 
				"				<connectionExpression><![CDATA[$P{REPORT_CONNECTION}]]></connectionExpression>\r\n" + 
				"				<subreportExpression><![CDATA[$P{"+nameSubreport+"}]]></subreportExpression>\r\n" + 
				"			</subreport>\r\n" + 
				"		</band>\r\n	");
		return report;
	}
	
	private static String replaceFooter(String report) throws Exception {
		
		report = report.replace("<queryString", "<parameter name=\"P_TOKEN\" class=\"java.lang.String\"/>\r\n" +
				"	<parameter name=\"REPORTE_PIE_PAGINA\" class=\"net.sf.jasperreports.engine.JasperReport\"/>\r\n "+
				"   <queryString");
		int inicio = report.indexOf("<pageFooter>");
		if(inicio<0) throw new Exception("No se encuentra el inicio del tag <pageFooter> ");
		int fin = report.substring(inicio+1).indexOf("</pageFooter>");
		if(fin<0) throw new Exception("No se encuentra el fin del tag </pageFooter> ");
		report = report.replace(report.substring(inicio, inicio + fin), "<pageFooter>\r\n"+ 
				"		<band height=\"20\" splitType=\"Stretch\">\r\n" + 
				"			<subreport>\r\n" + 
				"				<reportElement x=\"0\" y=\"0\" width=\"572\" height=\"20\" uuid=\"05fdcc2b-1e35-49dc-8aea-6e9edde5f626\"/>\r\n" + 
				"				<subreportParameter name=\"P_NUMBER_PAGE\">\r\n" + 
				"					<subreportParameterExpression><![CDATA[$V{PAGE_NUMBER}]]></subreportParameterExpression>\r\n" + 
				"				</subreportParameter>\r\n" + 
				"				<subreportParameter name=\"P_TOKEN\">\r\n" + 
				"					<subreportParameterExpression><![CDATA[$P{P_TOKEN}]]></subreportParameterExpression>\r\n" + 
				"				</subreportParameter>\r\n" + 
				"				<connectionExpression><![CDATA[$P{REPORT_CONNECTION}]]></connectionExpression>\r\n" + 
				"				<subreportExpression><![CDATA[$P{REPORTE_PIE_PAGINA}]]></subreportExpression>\r\n" + 
				"			</subreport>\r\n" + 
				"		</band>\r\n	");
		return report;
	}
	
	public static byte[] exportarReportePDF(String reportejrxml, Map<String, Object> parametrosReporte, Connection conexion)throws Exception {
		ByteArrayOutputStream vByteOutputStream = new ByteArrayOutputStream();
		try {
			if(parametrosReporte.containsKey(REPORTE_ENCABEZADO)) {
				JasperReport encabezado = JasperCompileManager.compileReport(new ByteArrayInputStream(parametrosReporte.get(REPORTE_ENCABEZADO).toString().getBytes("utf-8")));
				parametrosReporte.put(REPORTE_ENCABEZADO, encabezado);
				reportejrxml = replaceHeader(reportejrxml, REPORTE_ENCABEZADO);
			}
			if(parametrosReporte.containsKey(REPORTE_PIE_PAGINA)) {
				JasperReport piePage = JasperCompileManager.compileReport(new ByteArrayInputStream(parametrosReporte.get(REPORTE_PIE_PAGINA).toString().getBytes("utf-8")));
				parametrosReporte.put(REPORTE_PIE_PAGINA, piePage);
				reportejrxml = replaceFooter(reportejrxml);
			}
			for (Map.Entry<String, Object> entry: parametrosReporte.entrySet()) {
				if(entry.getKey().startsWith(P_SUBREPORT)) {
					JasperReport jasperSubReport = JasperCompileManager.compileReport(new ByteArrayInputStream(entry.getValue().toString().getBytes("utf-8")));
					parametrosReporte.put(entry.getKey(), jasperSubReport);
					reportejrxml = replaceReport(reportejrxml,jasperSubReport.getName(), entry.getKey());
				}
			}
			
			JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(reportejrxml.getBytes("utf-8")));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametrosReporte, conexion);

			SimplePdfExporterConfiguration configuration = null;
			if(jasperPrint.getPages().size()!=0){
				configuration = new SimplePdfExporterConfiguration();
				if(parametrosReporte.get("P_DIALOG_PRINT")!= null)
					configuration.setPdfJavaScript("this.print();");
				if(parametrosReporte.get("P_PRINTER_NAME")!= null){
					configuration.setPdfJavaScript("var params = this.getPrintParams();params.interactive=params.constants.interactionLevel.silent;params.pageHandling=params.constants.handling.none;params.printerName='"+parametrosReporte.get("P_PRINTER_NAME")+"';this.print(params);");
				}else{
					if(parametrosReporte.get("P_AUTO_PRINT")!= null)
						configuration.setPdfJavaScript("this.print({bUI: false, bSilent: true, bShrinkToFit: false});\r\nthis.closeDoc(true);");
				}
				if(parametrosReporte.get("PDF_JAVASCRIPT")!= null)
					configuration.setPdfJavaScript(parametrosReporte.get("PDF_JAVASCRIPT").toString());
				
			}else{
				return null;
			}
			JRPdfExporter vXlsExporter = new JRPdfExporter();
			vXlsExporter.setExporterInput(new SimpleExporterInput( jasperPrint));
			vXlsExporter.setExporterOutput(new  SimpleOutputStreamExporterOutput(vByteOutputStream));
			vXlsExporter.setConfiguration(configuration);
			vXlsExporter.exportReport();		
			vByteOutputStream.close();	
			return vByteOutputStream.toByteArray();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public static byte[] exportarReporteHTML(String reportejrxml, Map<String, Object> parametrosReporte, Connection conexion) throws Exception {
		try {
			
			for (Map.Entry<String, Object> entry: parametrosReporte.entrySet()) {
				if(entry.getKey().startsWith(P_SUBREPORT)) {
					JasperReport jasperSubReport = JasperCompileManager.compileReport(new ByteArrayInputStream(entry.getValue().toString().getBytes("utf-8")));
					parametrosReporte.put(entry.getKey(), jasperSubReport);
					reportejrxml = replaceReport(reportejrxml,jasperSubReport.getName(), entry.getKey());
				}
			}
			
			JasperReport jasperReport = JasperCompileManager.compileReport(new ByteArrayInputStream(reportejrxml.getBytes("utf-8")));
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parametrosReporte, conexion);
			ByteArrayOutputStream vByteOutputStream = new ByteArrayOutputStream();
			HtmlExporter vHTMLExporter = new HtmlExporter();
			vHTMLExporter.setExporterInput(new SimpleExporterInput( jasperPrint));
			vHTMLExporter.setExporterOutput(new  SimpleHtmlExporterOutput(vByteOutputStream));
			/*SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setCollapseRowSpan(true);
			configuration.setDetectCellType(true);
			configuration.setIgnoreGraphics(false);
			configuration.setRemoveEmptySpaceBetweenColumns(true);
			configuration.setRemoveEmptySpaceBetweenRows(true);
			configuration.setWhitePageBackground(false);
			configuration.setWrapText(true);
			vHTMLExporter.setConfiguration(configuration);
			*/
			vHTMLExporter.exportReport();
			vByteOutputStream.close();	
			return vByteOutputStream.toByteArray();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
}