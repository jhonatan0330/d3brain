package d3.report.application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import d3.shared.domain.ServerException;

public class ReportRowsFromSql {

	public static List<Map<String, Object>> consultar(String sql, Map<String, Object> mapParams, Connection conexion)
			throws ServerException {
		if (conexion == null)
			throw new ServerException("La conexion a base de datos se encuentra nula");
		if (sql == null || sql.trim().isEmpty())
			throw new ServerException("La consulta SQL del reporte se encuentra vacia");
		sql = reemplazarParametros(sql, mapParams);
		List<Map<String, Object>> filas = new ArrayList<Map<String, Object>>();
		try (Statement statement = conexion.createStatement(); ResultSet result = statement.executeQuery(sql)) {
			ResultSetMetaData metaData = result.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			String[] columnas = new String[numberOfColumns + 1];
			for (int i = 1; i <= numberOfColumns; i++) {
				columnas[i] = normalizarColumna(metaData.getColumnName(i));
			}
			while (result.next()) {
				Map<String, Object> fila = new HashMap<String, Object>();
				for (int i = 1; i <= numberOfColumns; i++) {
					fila.put(columnas[i], result.getObject(i));
				}
				filas.add(fila);
			}
			return filas;
		} catch (SQLException e) {
			throw new ServerException("Error ejecutando la consulta del reporte: " + e.getMessage());
		}
	}

	private static String reemplazarParametros(String sql, Map<String, Object> mapParams) {
		if (mapParams != null) {
			for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
				String codeToEvaluate = "$P{" + entry.getKey() + "}";
				String valor = entry.getValue() == null ? "null"
						: "'" + entry.getValue().toString().replace("'", "''") + "'";
				while (sql.contains(codeToEvaluate)) {
					sql = sql.replace(codeToEvaluate, valor);
				}
			}
		}
		return sql.replaceAll("\\$P\\{[A-Za-z0-9_/():\\-\\[\\]]*\\}", "null");
	}

	private static String normalizarColumna(String nombre) {
		if (nombre == null)
			return "columna";
		String normalizado = nombre.toLowerCase().replaceAll("[^a-z0-9_]", "_");
		if (normalizado.isEmpty())
			return "columna";
		return normalizado;
	}

}