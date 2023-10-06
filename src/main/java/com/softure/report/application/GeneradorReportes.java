package com.softure.report.application;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;


public class GeneradorReportes {

	private Connection conexion ;
	
	public GeneradorReportes(Connection conexionSource) throws Exception {
		if ( conexionSource == null ) throw new Exception("Llave conexion esta nula", null);
		conexion = conexionSource;
	}

	public byte[] generarReporteExcel(String pNombreReporte,Map<String, Object> pParametrosReporte) throws Exception {
		if(pParametrosReporte!=null) {
			pParametrosReporte.put(JRParameter.IS_IGNORE_PAGINATION, true);
		}
		try {
			return ReportesUtil.exportarReporteExcel(pNombreReporte, pParametrosReporte, conexion);
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

	public byte[] generarReportePDF(String pNombreReporte,	Map<String, Object> pParametrosReporte) throws Exception {
		try {
			return ReportesUtil.exportarReportePDF(pNombreReporte, pParametrosReporte, conexion);
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
	
	public byte[] generarReporteHTML(String pNombreReporte,	Map<String, Object> pParametrosReporte) throws Exception {
		try {
			return ReportesUtil.exportarReporteHTML(pNombreReporte, pParametrosReporte, conexion);
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
}
