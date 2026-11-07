package d3.multitenancy.application;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.stereotype.Component;

import d3.multitenancy.domain.TenantDTO;
import d3.multitenancy.domain.TenantMetadataProvider;
import d3.shared.domain.ServerException;

/**
 * Centraliza la ejecución de sentencias SQL sobre las bases de datos de los
 * tenants, incluyendo la resolución de su configuración JDBC y la gestión de
 * conexiones.
 */
@Component
public class TenantSqlExecutor {

	private final TenantDataSourceFactory dataSourceFactory;
	private final TenantMetadataProvider metadataProvider;

	public TenantSqlExecutor(TenantDataSourceFactory dataSourceFactory, TenantMetadataProvider metadataProvider) {
		this.dataSourceFactory = dataSourceFactory;
		this.metadataProvider = metadataProvider;
	}

	public void actualizarComentarioTabla(String tenantId,  String fechaIso)
			throws ServerException {
		TenantDTO tenant = metadataProvider.resolve(tenantId)
				.orElseThrow(() -> new ServerException(
						"No se encontro la configuracion de conexion del tenant " + tenantId));
		actualizarComentarioTabla(tenant, fechaIso);
	}

	public void actualizarComentarioTabla(TenantDTO tenant, String fechaIso)
			throws ServerException {
		String sql = "COMMENT ON TABLE public.usuarioautenticacion_uaup IS '" + fechaIso + "'";
		ejecutar(tenant, sql);
	}

	public void ejecutar(String tenantId, String sql) throws ServerException {
		TenantDTO tenant = metadataProvider.resolve(tenantId)
				.orElseThrow(() -> new ServerException(
						"No se encontro la configuracion de conexion del tenant " + tenantId));
		ejecutar(tenant, sql);
	}

	public void ejecutar(TenantDTO tenant, String sql) throws ServerException {
		if (tenant == null || tenant.getDatasourceUrl() == null || tenant.getDatasourceUrl().isBlank()) {
			throw new ServerException("El tenant no tiene configurada la conexion a su base de datos");
		}
		PooledDataSource dataSource = dataSourceFactory.createPooledDataSource(tenant);
		try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
			stmt.execute(sql);
		} catch (SQLException e) {
			throw new ServerException(
					"No se pudo ejecutar la sentencia en la base de datos del tenant: " + e.getMessage(), e);
		} finally {
			dataSource.forceCloseAll();
		}
	}


}
