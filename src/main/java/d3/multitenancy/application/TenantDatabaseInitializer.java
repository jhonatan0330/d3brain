package d3.multitenancy.application;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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
			System.out.println("*********************************************************");
			System.out.println("Inicializando tenant: " + tenant);
			System.out.println("*********************************************************");
			doSomethingAfterStartup(tenant);
		});
	}

	private void doSomethingAfterStartup(String tenantId) {
		DataSource tenantDs = metadataProvider.resolve(tenantId).map(dto -> routingDataSource)
				.orElseThrow(() -> new IllegalStateException("Tenant no encontrado: " + tenantId));
		String actualString = scriptExecutor.leerVersionActual(tenantDs);
		System.out.println("Fecha actual BD [" + tenantId + "] = " + actualString);
		scriptExecutor.ejecutarPendientesHastaHoy(tenantDs);
	}
}
