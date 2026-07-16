package com.softure.multitenancy;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.softure.multitenancy.domain.TenantDTO;

/**
 * Wires lazy {@link TenantRoutingDataSource}, servlet filter, and tenant
 * registry backed by {@link TenantMetadataProvider}.
 */
@Configuration
@EnableConfigurationProperties(TenantDataSourcesConfigurationProperties.class)
public class TenantDataSourceConfiguration {

	@Bean
	TenantFilter tenantFilter(TenantDataSourcesConfigurationProperties tenantProperties,
			TenantRegistry tenantRegistry) {
		return new TenantFilter(tenantProperties, tenantRegistry);
	}

	@Bean
	FilterRegistrationBean<TenantFilter> tenantFilterRegistration(TenantFilter tenantFilter) {
		FilterRegistrationBean<TenantFilter> reg = new FilterRegistrationBean<>();
		reg.setFilter(tenantFilter);
		reg.addUrlPatterns("/*");
		reg.setOrder(Ordered.HIGHEST_PRECEDENCE + 10);
		return reg;
	}

	@Bean(name = "dataSource")
	@Primary
	DataSource dataSource(TenantMetadataProvider metadataProvider, TenantDataSourceFactory factory,
			TenantDataSourcesConfigurationProperties props, Environment env) {
		// TenantDTO defaultJdbc =
		// metadataProvider.resolve(props.getDefaultTenantId()).orElseThrow(
		// () -> new IllegalStateException("Default tenant '" +
		// props.getDefaultTenantId() + "' not in catalog"));
		// PooledDataSource defaultPool = factory.createPooledDataSource(defaultJdbc);
		PooledDataSource defaultPool = buildDefaultFromEnv(factory, env);
		return new TenantRoutingDataSource(metadataProvider, factory, props, defaultPool);
	}

	private PooledDataSource buildDefaultFromEnv(TenantDataSourceFactory factory, Environment env) {
		TenantDTO dto = new TenantDTO();
		dto.setDriver(env.getProperty("db.driver"));
		dto.setDatasourceUrl(env.getProperty("db.url"));
		dto.setDatasourceUsername(env.getProperty("db.username"));
		dto.setDatasourcePassword(env.getProperty("db.password"));
		return factory.createPooledDataSource(dto);
	}

	@Bean // ✅ TransactionManager aquí, no en una config separada
	PlatformTransactionManager transactionManager(DataSource dataSource) {
		return new DataSourceTransactionManager(dataSource);
	}
}
