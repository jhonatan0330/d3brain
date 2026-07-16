package com.softure.report.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import com.shared.domain.SharedConstants;

import net.sf.jasperreports.engine.JRParameter;

public class GeneradorReportes {

	private Connection conexion;
	private JasperReportCache cache;
	private String reportKey;
	private boolean isRemote = false;

	public Connection getConexion() {
		return conexion;
	}

	public GeneradorReportes(Connection conexionSource, JasperReportCache pCache, String pReportKey) throws Exception {
		if (conexionSource == null)
			throw new Exception("Llave conexion esta nula", null);
		conexion = conexionSource;
		this.cache = pCache;
		this.reportKey = pReportKey;
	}

	public GeneradorReportes(String dataSource) throws Exception {
		String[] grupos = dataSource.split(SharedConstants.PUNTO_COMA_DOBLE);
		String url = grupos[0];
		String user = grupos[1];
		String password = grupos[2];
		try {
			// Cargar el driver JDBC de SQL Server
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conexion = DriverManager.getConnection(url, user, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public byte[] generarReporteExcel(String pNombreReporte, Map<String, Object> pParametrosReporte) throws Exception {
		if (pParametrosReporte != null) {
			pParametrosReporte.put(JRParameter.IS_IGNORE_PAGINATION, true);
		}
		try {
			return ReportesUtil.exportarReporteExcel(pNombreReporte, pParametrosReporte, conexion, cache, reportKey);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public byte[] generarReportePDF(String pNombreReporte, Map<String, Object> pParametrosReporte) throws Exception {
		try {
			return ReportesUtil.exportarReportePDF(pNombreReporte, pParametrosReporte, conexion, cache, reportKey);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public byte[] generarReporteHTML(String pNombreReporte, Map<String, Object> pParametrosReporte) throws Exception {
		try {
			return ReportesUtil.exportarReporteHTML(pNombreReporte, pParametrosReporte, conexion, cache, reportKey);
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (conexion != null) {
					conexion.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void closeConnection() {
		if (isRemote) {
			try {
				if (conexion != null)
					conexion.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
