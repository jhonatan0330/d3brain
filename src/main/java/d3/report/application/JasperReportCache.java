package d3.report.application;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.springframework.stereotype.Component;

import d3.CacheManager;
import d3.shared.application.D3Utils;
import d3.shared.domain.ServerException;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;

@Component
public class JasperReportCache {

	private final CacheManager cacheManager;

	public JasperReportCache(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	public JasperReport getReport(String reportejrxml, String pReportKey) throws ServerException {
		try {
			String reportName = D3Utils.formatFunction(pReportKey + "|" + sha256(reportejrxml));
			return cacheManager.getReportsMap().computeIfAbsent(reportName, key -> compilar(reportejrxml));
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Error compilando reporte");
		}
	}

	public void clearCache() {
		cacheManager.clearReports();
	}

	private JasperReport compilar(String reportejrxml) {
		try (InputStream compileStream = new ByteArrayInputStream(reportejrxml.getBytes(StandardCharsets.UTF_8))) {
			return JasperCompileManager.compileReport(compileStream);
		} catch (Exception e) {
			throw new RuntimeException("Error compiling report", e);
		}
	}

	private String sha256(String texto) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hash = digest.digest(texto.getBytes(StandardCharsets.UTF_8));
		StringBuilder hexString = new StringBuilder();
		for (byte b : hash) {
			String hex = Integer.toHexString(0xff & b);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
