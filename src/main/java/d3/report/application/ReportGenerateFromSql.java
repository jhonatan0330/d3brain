package d3.report.application;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

public class ReportGenerateFromSql {

	public static byte[] call(String sql, Map<String, Object> mapParams, Connection conexion) throws ServerException {

		for (Map.Entry<String, Object> entry : mapParams.entrySet()) {
			String codeToEvaluate = "$P{" + entry.getKey() + "}";
			while (sql.contains(codeToEvaluate)) {
				sql = sql.replace(codeToEvaluate, "'" + entry.getValue().toString() + "'");
			}
		}
		sql = sql.replaceAll("\\$P\\{[A-Za-z0-9_/():\\-\\[\\]]*\\}", "null");
		String resultCSV = "";
		Statement statement;
		try {
			statement = conexion.createStatement();
			ResultSet result = statement.executeQuery(sql);

			// write header line containing column names
			ResultSetMetaData metaData = result.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			String headerLine = "";

			// exclude the first column which is the ID field
			for (int i = 1; i <= numberOfColumns; i++) {
				String columnName = metaData.getColumnName(i);
				headerLine = headerLine.concat(columnName.toUpperCase()).concat(";");
			}

			resultCSV = headerLine.substring(0, headerLine.length() - 1);

			while (result.next()) {
				String line = "";

				for (int i = 1; i <= numberOfColumns; i++) {
					Object valueObject = result.getObject(i);
					String valueString = "";
					if (valueObject != null)
						valueString = valueObject.toString();

					if (valueObject instanceof String) {
						valueString = "\"" + valueString.replaceAll("\"", "\"\"") + "\"";
					}

					line = line.concat(valueString);

					if (i != numberOfColumns) {
						line = line.concat(";");
					}
				}
				resultCSV = resultCSV + SharedConstants.NEW_LINE + line;
				// System.out.println("Rows: " + result.getRow() + " " + line);
			}

			statement.close();

			headerLine = "";
			for (int i = 1; i <= numberOfColumns; i++) {
				headerLine = headerLine.concat("").concat(";");
			}
			resultCSV = resultCSV + SharedConstants.NEW_LINE + headerLine;

		} catch (SQLException e) {
			throw new ServerException(e.getMessage());
		}
		return ("\uFEFF" + resultCSV).getBytes(StandardCharsets.UTF_8);

	}

}
