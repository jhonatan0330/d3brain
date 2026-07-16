package com.softure;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.ScriptException;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import com.softure.multitenancy.TenantContext;
import com.softure.multitenancy.TenantIteratorService;
import com.softure.multitenancy.TenantMetadataProvider;

@Component
@Order(2) // ✅ después de que DatabaseTenantMetadataProvider cargue el catalog (Order 1)
public class TenantDatabaseInitializer implements ApplicationRunner {

	private final TenantIteratorService tenantIteratorService;
	private final DataSource routingDataSource;
	private final TenantMetadataProvider metadataProvider;

	public TenantDatabaseInitializer(@Lazy TenantIteratorService tenantIteratorService,
			@Qualifier("dataSource") DataSource routingDataSource, TenantMetadataProvider metadataProvider) {
		this.tenantIteratorService = tenantIteratorService;
		this.routingDataSource = routingDataSource;
		this.metadataProvider = metadataProvider;
	}

	@Override
	public void run(ApplicationArguments args) {
		tenantIteratorService.executeForAllTenants(tenant -> {
			System.out.println("*********************************************************");
			System.out.println("Inicializando tenant: " + tenant);
			System.out.println("*********************************************************");
			doSomethingAfterStartup(tenant);
		});
	}

	private void doSomethingAfterStartup(String tenantId) {

		// ── 1. Obtener DataSource del tenant ──────────────────────────────────
		DataSource tenantDs = metadataProvider.resolve(tenantId).map(dto -> (DataSource) routingDataSource)
				.orElseThrow(() -> new IllegalStateException("Tenant no encontrado: " + tenantId));

		// ── 2. Leer fecha actual de la BD del tenant ──────────────────────────
		String actualString = getActualDate(tenantDs);
		System.out.println("Fecha actual BD [" + tenantId + "] = " + actualString);

		if (actualString == null) {
			printError();
			return;
		}

		// ── 3. Parsear fecha ──────────────────────────────────────────────────
		Date actualDate;
		try {
			actualDate = new SimpleDateFormat("yyyy-MM-dd").parse(actualString);
		} catch (ParseException e) {
			printError();
			System.out.println(e.getMessage());
			return;
		}

		System.out.println("Fecha actual en BD = " + actualDate);

		// ── 4. Iterar días y ejecutar scripts SQL ─────────────────────────────
		Calendar iterador = Calendar.getInstance();
		iterador.setTime(actualDate);
		iterador.add(Calendar.DAY_OF_MONTH, 1);

		System.out.println("*********************************************************");
		System.out.println("************ COMIENZA A ACTUALIZAR **********************");
		System.out.println("*********************************************************");

		boolean error = false;
		while (iterador.getTime().getTime() < new Date().getTime() && !error) {
			String sqlName = buildSqlPath(iterador);
			Resource fileSql = new ClassPathResource(sqlName);

			if (fileSql.exists()) {
				System.out.println("Ejecutando Script = " + sqlName + " -> " + new Date());
				error = executeScript(tenantDs, fileSql);
			}

			iterador.add(Calendar.DAY_OF_MONTH, 1);
		}

		// ── 5. Resultado ──────────────────────────────────────────────────────
		if (!error) {
			printSuccess();
		} else {
			printScriptError();
		}
	}

	// ── Helpers ───────────────────────────────────────────────────────────────

	private boolean executeScript(DataSource ds, Resource fileSql) {
		// TransactionManager apuntando directo al DataSource del tenant
		DataSourceTransactionManager tm = new DataSourceTransactionManager(ds);

		return Boolean.TRUE.equals(new TransactionTemplate(tm).execute(ts -> {
			Connection conn = null;
			try {
				conn = ds.getConnection();
				conn.setAutoCommit(false);
				ScriptUtils.executeSqlScript(conn, new EncodedResource(fileSql, "UTF-8"));
				conn.commit();
				return false; // sin error
			} catch (ScriptException | SQLException e) {
				System.out.println(e.getMessage());
				try {
					if (conn != null)
						conn.rollback();
				} catch (SQLException ex) {
					System.out.println(ex.getMessage());
				}
				return true; // con error
			} finally {
				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException ex) {
						System.out.println(ex.getMessage());
					}
				}
			}
		}));
	}

	private String getActualDate(DataSource ds) {
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
		System.out.println("*******                SOFTURE                   ********");
		System.out.println("*******     LLAMA YA AL SOFTWARE PARA TI .COM    ********");
		System.out.println("*******                                          ********");
		System.out.println("*********************************************************");
	}

	private void printSuccess() {
		System.out.println("*******OKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOKOOKOKOKOKO********");
		System.out.println("*******                SOFTURE                   ********");
		System.out.println("*******     LO HEMOS LOGRADO TODO ACTUALIZADO    ********");
		System.out.println("*******                                          ********");
		System.out.println("****************:)****:)***:)***:)***:)******************");
	}

	private void printScriptError() {
		System.out.println("*********************************************************");
		System.out.println("*******     ERROR       SOFTURE     ERROR        ********");
		System.out.println("*******     LLAMA YA AL SOFTWARE PARA TI .COM    ********");
		System.out.println("*******                                          ********");
		System.out.println("********!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!********");
		System.out.println("********XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX*********");
	}
}