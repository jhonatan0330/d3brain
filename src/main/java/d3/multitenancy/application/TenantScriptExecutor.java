package d3.multitenancy.application;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import d3.multitenancy.domain.TenantProvisionFase;
import d3.shared.domain.ServerException;

@Component
public class TenantScriptExecutor {

	private static final Pattern FECHA_VERSION = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

	public boolean ejecutarPendientesHastaHoy(DataSource tenantDs) {
		String actualString = leerVersionActual(tenantDs);
		if (actualString == null) {
			printError();
			return true;
		}
		Date actualDate;
		try {
			actualDate = new SimpleDateFormat("yyyy-MM-dd").parse(actualString);
		} catch (ParseException e) {
			printError();
			System.out.println(e.getMessage());
			return true;
		}
		System.out.println("Fecha actual en BD = " + actualDate);
		System.out.println("*********************************************************");
		System.out.println("************ COMIENZA A ACTUALIZAR **********************");
		System.out.println("*********************************************************");
		boolean error = false;
		Calendar iterador = Calendar.getInstance();
		iterador.setTime(actualDate);
		iterador.add(Calendar.DAY_OF_MONTH, 1);
		while (iterador.getTime().getTime() < new Date().getTime() && !error) {
			String sqlName = buildSqlPath(iterador);
			Resource fileSql = new ClassPathResource(sqlName);
			if (fileSql.exists()) {
				System.out.println("Ejecutando Script = " + sqlName + " -> " + new Date());
				error = executeScript(tenantDs, fileSql);
			}
			iterador.add(Calendar.DAY_OF_MONTH, 1);
		}
		if (!error) {
			printSuccess();
		} else {
			printScriptError();
		}
		return error;
	}

	public void ejecutarPendientesHastaHoyExigente(DataSource tenantDs) throws ServerException {
		String actualString = leerVersionActual(tenantDs);
		if (actualString == null) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.SCRIPTS_DELTAS,
					"La base de datos nueva no reporta version (COMMENT ON TABLE usuario_usrp)");
		}
		Date actualDate;
		try {
			actualDate = new SimpleDateFormat("yyyy-MM-dd").parse(actualString);
		} catch (ParseException e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.SCRIPTS_DELTAS,
					"La version de la base de datos nueva no es una fecha valida: " + actualString, e);
		}
		Calendar iterador = Calendar.getInstance();
		iterador.setTime(actualDate);
		iterador.add(Calendar.DAY_OF_MONTH, 1);
		while (iterador.getTime().getTime() < new Date().getTime()) {
			String sqlName = buildSqlPath(iterador);
			Resource fileSql = new ClassPathResource(sqlName);
			if (fileSql.exists()) {
				System.out.println("Ejecutando Script = " + sqlName + " -> " + new Date());
				ejecutarScriptExigente(tenantDs, fileSql);
			}
			iterador.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	public void ejecutarScriptExigente(DataSource ds, Resource fileSql) throws ServerException {
		DataSourceTransactionManager tm = new DataSourceTransactionManager(ds);
		try {
			new TransactionTemplate(tm).executeWithoutResult(ts -> {
				Connection conn = DataSourceUtils.getConnection(ds);
				try {
					ScriptUtils.executeSqlScript(conn, new EncodedResource(fileSql, "UTF-8"));
				} catch (ScriptException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			});
		} catch (RuntimeException e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.SCRIPTS_DELTAS,
					"Fallo ejecutando " + describirRecurso(fileSql), e);
		}
	}

	public void sellarVersionBase(DataSource ds, String fecha) throws ServerException {
		if (fecha == null || !FECHA_VERSION.matcher(fecha).matches()) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.SELLO_VERSION,
					"La fecha base de version no es valida: " + fecha);
		}
		try (Connection conn = ds.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.execute("COMMENT ON TABLE usuario_usrp IS '" + fecha + "'");
		} catch (Exception e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.SELLO_VERSION,
					"No se pudo sellar la version base " + fecha + " en la base de datos nueva", e);
		}
	}

	public String leerVersionActual(DataSource ds) {
		String result = null;
		try (Connection conn = ds.getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("select description from pg_description "
						+ "join pg_class on pg_description.objoid = pg_class.oid "
						+ "join pg_namespace on pg_class.relnamespace = pg_namespace.oid "
						+ "where relname = 'usuario_usrp';")) {
			while (rs.next()) {
				result = rs.getString("description");
			}
		} catch (Exception e) {
			System.err.println(
					"Error leyendo fecha en tenant " + TenantContext.getCurrentTenant() + ": " + e.getMessage());
		}
		return result;
	}

	private String describirRecurso(Resource fileSql) {
		try {
			String nombre = fileSql.getFilename();
			return nombre != null ? nombre : fileSql.getDescription();
		} catch (Exception e) {
			return fileSql.getDescription();
		}
	}

	private boolean executeScript(DataSource ds, Resource fileSql) {
		DataSourceTransactionManager tm = new DataSourceTransactionManager(ds);
		try {
			new TransactionTemplate(tm).executeWithoutResult(ts -> {
				Connection conn = DataSourceUtils.getConnection(ds);
				try {
					ScriptUtils.executeSqlScript(conn, new EncodedResource(fileSql, "UTF-8"));
				} catch (ScriptException e) {
					throw new RuntimeException("Error ejecutando " + describirRecurso(fileSql) + ": " + e.getMessage(),
							e);
				}
			});
			return false;
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			return true;
		}
	}

	private String buildSqlPath(Calendar cal) {
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String month = to2String(cal.get(Calendar.MONTH) + 1);
		String day = to2String(cal.get(Calendar.DAY_OF_MONTH));
		return "static/data/" + year + "/" + year + month + "/" + year + month + day + ".sql";
	}

	private String to2String(int value) {
		return value < 10 ? "0" + value : String.valueOf(value);
	}

	private void printError() {
		System.out.println("*********************************************************");
		System.out.println("*******                ERROR                     ********");
		System.out.println("*******                                          ********");
		System.out.println("*********************************************************");
	}

	private void printSuccess() {
		System.out.println("*******OKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOOKOKOKOKO********");
		System.out.println("*******     LO HEMOS LOGRADO TODO ACTUALIZADO    ********");
		System.out.println("*******                                          ********");
		System.out.println("****************:)****:)***:)***:)***:)******************");
	}

	private void printScriptError() {
		System.out.println("*********************************************************");
		System.out.println("*******     ERROR                   ERROR        ********");
		System.out.println("*******                                          ********");
		System.out.println("********!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!********");
		System.out.println("********XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*********");
	}
}
