package d3.multitenancy.application;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantMetadataProvider;

@Component
@Order(2)
public class TenantDatabaseInitializer implements ApplicationRunner {

	private final TenantIteratorService tenantIteratorService;
	private final DataSource routingDataSource;
	private final TenantMetadataProvider metadataProvider;
	private final TenantScriptExecutor scriptExecutor;

	public TenantDatabaseInitializer(@Lazy TenantIteratorService tenantIteratorService,
			@Qualifier("dataSource") DataSource routingDataSource, TenantMetadataProvider metadataProvider,
			TenantScriptExecutor scriptExecutor) {
		this.tenantIteratorService = tenantIteratorService;
		this.routingDataSource = routingDataSource;
		this.metadataProvider = metadataProvider;
		this.scriptExecutor = scriptExecutor;
	}

	@Override
	public void run(ApplicationArguments args) {
		tenantIteratorService.executeForAllTenants(tenant -> {
			TenantDTO datos = metadataProvider.resolve(tenant)
					.orElseThrow(() -> new IllegalStateException("Tenant no encontrado: " + tenant));
			System.out.println("********** Inicializando tenant: " + nombreTenant(datos, tenant) +" **********");
			doSomethingAfterStartup(datos, tenant);
		});
	}

	private void doSomethingAfterStartup(TenantDTO datos, String tenantId) {
		DataSource tenantDs = routingDataSource;
		String tenantName = datos.getName();
		String actualString = scriptExecutor.leerVersionActual(tenantDs, tenantId, tenantName);
		System.out.println("Fecha actual BD [" + nombreTenant(datos, tenantId) + "] = " + actualString);
		scriptExecutor.ejecutarPendientesHastaHoy(tenantDs, tenantId, tenantName);
	}

	private String nombreTenant(TenantDTO datos, String tenantId) {
		String nombre = datos != null ? datos.getName() : null;
		if (nombre == null || nombre.isBlank()) {
			return tenantId;
		}
		return nombre.trim() + " (id=" + tenantId + ")";
	}
}
