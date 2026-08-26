package d3.report.application;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import d3.shared.domain.ServerException;
import d3.java.services.D3Utils;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

@Component
public class JasperReportCache {

	private final Map<String, JasperReport> cache = new ConcurrentHashMap<>();

	public JasperReport getReport(String reportejrxml, String pReportKey) throws ServerException {
		try {
			// 1. Extraer UUID
			String reportName = D3Utils
					.formatFunction(pReportKey + readUUID(new ByteArrayInputStream(reportejrxml.getBytes("UTF-8"))));

			// 2. Compilar y cachear si no existe
			return cache.computeIfAbsent(reportName, key -> {
				try (InputStream compileStream = new ByteArrayInputStream(reportejrxml.getBytes("UTF-8"))) {
					return JasperCompileManager.compileReport(compileStream);
				} catch (Exception e) {
					throw new RuntimeException("Error compiling report", e);
				}
			});

		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Error reading report UUID");
		}
	}

	public void clearCache() {
		cache.clear();
	}

	public void removeReport(String reportName) {
		cache.remove(reportName);
	}

	private String readUUID(InputStream pIoStream) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // Jasper usa xmlns
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(pIoStream);
		Element jasperReport = doc.getDocumentElement();
		return jasperReport.getAttribute("uuid");
	}
}
