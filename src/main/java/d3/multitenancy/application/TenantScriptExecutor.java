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

import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantMetadataProvider;
import d3.multitenancy.domain.TenantProvisionFase;
import d3.shared.domain.ServerException;

@Component
public class TenantScriptExecutor {

	private static final Pattern FECHA_VERSION = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

	private final TenantMetadataProvider metadataProvider;

	public TenantScriptExecutor(TenantMetadataProvider metadataProvider) {
		this.metadataProvider = metadataProvider;
	}

	public boolean ejecutarPendientesHastaHoy(DataSource tenantDs) {
		return ejecutarPendientesHastaHoy(tenantDs, TenantContext.getCurrentTenant(), null);
	}

	public boolean ejecutarPendientesHastaHoy(DataSource tenantDs, String tenantId) {
		return ejecutarPendientesHastaHoy(tenantDs, tenantId, null);
	}

	public boolean ejecutarPendientesHastaHoy(DataSource tenantDs, String tenantId, String tenantName) {
		String etiqueta = etiquetaTenant(tenantId, tenantName);
		String actualString = leerVersionActual(tenantDs, tenantId, tenantName);
		if (actualString == null) {
			printError(etiqueta, "No se pudo leer la version actual de la base de datos");
			return true;
		}
		Date actualDate;
		try {
			actualDate = new SimpleDateFormat("yyyy-MM-dd").parse(actualString);
		} catch (ParseException e) {
			printError(etiqueta, "La version de la base de datos no es una fecha valida: " + actualString);
			return true;
		}
		System.out.println(etiqueta + " Fecha actual en BD = " + actualString);
		System.out.println(etiqueta + " *********************************************************");
		System.out.println(etiqueta + " ************ COMIENZA A ACTUALIZAR **********************");
		System.out.println(etiqueta + " *********************************************************");
		boolean error = false;
		Calendar iterador = Calendar.getInstance();
		iterador.setTime(actualDate);
		iterador.add(Calendar.DAY_OF_MONTH, 1);
		while (iterador.getTime().getTime() < new Date().getTime() && !error) {
			String sqlName = buildSqlPath(iterador);
			Resource fileSql = new ClassPathResource(sqlName);
			if (fileSql.exists()) {
				System.out.println(etiqueta + " Ejecutando Script = " + sqlName + " -> " + new Date());
				error = executeScript(tenantDs, fileSql, tenantId, tenantName);
			}
			iterador.add(Calendar.DAY_OF_MONTH, 1);
		}
		if (!error) {
			printSuccess(etiqueta);
		} else {
			printScriptError(etiqueta);
		}
		return error;
	}

	public void ejecutarPendientesHastaHoyExigente(DataSource tenantDs) throws ServerException {
		ejecutarPendientesHastaHoyExigente(tenantDs, TenantContext.getCurrentTenant(), null);
	}

	public void ejecutarPendientesHastaHoyExigente(DataSource tenantDs, String tenantId) throws ServerException {
		ejecutarPendientesHastaHoyExigente(tenantDs, tenantId, null);
	}

	public void ejecutarPendientesHastaHoyExigente(DataSource tenantDs, String tenantId, String tenantName)
			throws ServerException {
		String etiqueta = etiquetaTenant(tenantId, tenantName);
		String actualString = leerVersionActual(tenantDs, tenantId, tenantName);
		if (actualString == null) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.SCRIPTS_DELTAS,
					etiqueta + " La base de datos nueva no reporta version (COMMENT ON TABLE usuario_usrp)");
		}
		Date actualDate;
		try {
			actualDate = new SimpleDateFormat("yyyy-MM-dd").parse(actualString);
		} catch (ParseException e) {
			throw TenantProvisionFase.fallar(TenantProvisionFase.SCRIPTS_DELTAS,
					etiqueta + " La version de la base de datos nueva no es una fecha valida: " + actualString, e);
		}
		System.out.println(etiqueta + " Fecha actual en BD = " + actualString);
		Calendar iterador = Calendar.getInstance();
		iterador.setTime(actualDate);
		iterador.add(Calendar.DAY_OF_MONTH, 1);
		while (iterador.getTime().getTime() < new Date().getTime()) {
			String sqlName = buildSqlPath(iterador);
			Resource fileSql = new ClassPathResource(sqlName);
			if (fileSql.exists()) {
				System.out.println(etiqueta + " Ejecutando Script = " + sqlName + " -> " + new Date());
				ejecutarScriptExigente(tenantDs, fileSql, tenantId, tenantName);
			}
			iterador.add(Calendar.DAY_OF_MONTH, 1);
		}
	}

	public void ejecutarScriptExigente(DataSource ds, Resource fileSql) throws ServerException {
		ejecutarScriptExigente(ds, fileSql, TenantContext.getCurrentTenant(), null);
	}

	public void ejecutarScriptExigente(DataSource ds, Resource fileSql, String tenantId) throws ServerException {
		ejecutarScriptExigente(ds, fileSql, tenantId, null);
	}

	public void ejecutarScriptExigente(DataSource ds, Resource fileSql, String tenantId, String tenantName)
			throws ServerException {
		String etiqueta = etiquetaTenant(tenantId, tenantName);
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
			System.err.println(etiqueta + " Fallo ejecutando " + describirRecurso(fileSql) + ": " + e.getMessage());
			throw TenantProvisionFase.fallar(TenantProvisionFase.SCRIPTS_DELTAS,
					etiqueta + " Fallo ejecutando " + describirRecurso(fileSql), e);
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
		return leerVersionActual(ds, TenantContext.getCurrentTenant(), null);
	}

	public String leerVersionActual(DataSource ds, String tenantId) {
		return leerVersionActual(ds, tenantId, null);
	}

	public String leerVersionActual(DataSource ds, String tenantId, String tenantName) {
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
			System.err.println(etiquetaTenant(tenantId, tenantName)
					+ " Error leyendo fecha en la base de datos del tenant: " + e.getMessage());
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

	private boolean executeScript(DataSource ds, Resource fileSql, String tenantId, String tenantName) {
		String etiqueta = etiquetaTenant(tenantId, tenantName);
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
			System.err.println(etiqueta + " Error ejecutando script " + describirRecurso(fileSql) + ": "
					+ e.getMessage());
			Throwable causa = e.getCause() != null ? e.getCause() : e;
			System.err.println(etiqueta + " Causa: " + causa.getClass().getName() + ": " + causa.getMessage());
			e.printStackTrace(System.err);
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

	private String etiquetaTenant(String tenantId, String tenantName) {
		String id = idTenant(tenantId);
		String nombre = nombreTenant(tenantId, tenantName);
		if (id == null) {
			return "[tenant=" + (nombre != null ? nombre : "desconocido") + "]";
		}
		if (nombre == null || nombre.equals(id)) {
			return "[tenant=" + id + "]";
		}
		return "[tenant=" + nombre + " (id=" + id + ")]";
	}

	private String idTenant(String tenantId) {
		String id = tenantId;
		if (id == null || id.isBlank()) {
			id = TenantContext.getCurrentTenant();
		}
		return id == null || id.isBlank() ? null : id.trim();
	}

	private String nombreTenant(String tenantId, String tenantName) {
		if (tenantName != null && !tenantName.isBlank()) {
			return tenantName.trim();
		}
		String id = idTenant(tenantId);
		if (id == null || metadataProvider == null) {
			return null;
		}
		try {
			return metadataProvider.resolve(id).map(TenantDTO::getName)
					.filter(nombre -> nombre != null && !nombre.isBlank()).map(String::trim).orElse(null);
		} catch (Exception e) {
			return null;
		}
	}

	private void printError(String etiqueta, String detalle) {
		System.out.println(etiqueta + " *********************************************************");
		System.out.println(etiqueta + " *******                ERROR                     ********");
		System.out.println(etiqueta + " Detalle: " + detalle);
		System.out.println(etiqueta + " *********************************************************");
	}

	private void printSuccess(String etiqueta) {
		System.out.println(etiqueta + " *******OKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOOKOKOKOKO********");
		System.out.println(etiqueta + " *******     LO HEMOS LOGRADO TODO ACTUALIZADO    ********");
		System.out.println(etiqueta + " *******                                          ********");
		System.out.println(etiqueta + " ****************:)****:)***:)***:)***:)******************");
	}

	private void printScriptError(String etiqueta) {
		System.out.println(etiqueta + " *********************************************************");
		System.out.println(etiqueta + " *******     ERROR                   ERROR        ********");
		System.out.println(etiqueta + " *******                                          ********");
		System.out.println(etiqueta + " ********!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!********");
		System.out.println(etiqueta + " ********XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*********");
	}
}
