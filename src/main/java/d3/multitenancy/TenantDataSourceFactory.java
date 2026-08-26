package d3.multitenancy;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import d3.multitenancy.domain.TenantDTO;

/**
 * Builds {@link PooledDataSource} instances from tenant JDBC metadata.
 * Centralizes driver fallbacks so production providers only supply URLs and
 * credentials.
 */
@Component
public class TenantDataSourceFactory {

	private final Environment env;

	public TenantDataSourceFactory(Environment env) {
		this.env = env;
	}

	public PooledDataSource createPooledDataSource(TenantDTO properties) {
		PooledDataSource ds = new PooledDataSource();
		String driver = properties.getDriver();
		if (driver == null || driver.isBlank()) {
			driver = env.getProperty("db.driver");
		}
		ds.setDriver(driver);
		ds.setUrl(properties.getDatasourceUrl());
		ds.setUsername(properties.getDatasourceUsername());
		ds.setPassword(properties.getDatasourcePassword());
		return ds;
	}
}
