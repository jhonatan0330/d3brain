package d3.multitenancy.application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TenantBaseAlias {

	private final Environment env;
	private final TenantDataSourcesConfigurationProperties props;

	private volatile String cachedBaseKey;

	public TenantBaseAlias(Environment env, TenantDataSourcesConfigurationProperties props) {
		this.env = env;
		this.props = props;
	}

	public String getDefaultTenantId() {
		String configured = props != null ? props.getDefaultTenantId() : null;
		return configured != null && !configured.isBlank() ? configured.trim() : "default";
	}

	public String getBasePublicKey() {
		String cached = cachedBaseKey;
		if (cached != null && !cached.isBlank()) {
			return cached;
		}
		synchronized (this) {
			if (cachedBaseKey != null && !cachedBaseKey.isBlank()) {
				return cachedBaseKey;
			}
			String resolved = cargarLlaveOrganizacionPrincipal();
			if (resolved != null && !resolved.isBlank()) {
				cachedBaseKey = resolved.trim();
				return cachedBaseKey;
			}
			return getDefaultTenantId();
		}
	}

	public boolean isBaseTenant(String tenantId) {
		if (tenantId == null || tenantId.isBlank()) {
			return false;
		}
		String normalizado = tenantId.trim();
		if (normalizado.equalsIgnoreCase(getDefaultTenantId())) {
			return true;
		}
		String base = getBasePublicKey();
		return normalizado.equals(base) || normalizado.equalsIgnoreCase(base);
	}

	public boolean isBaseSegment(String segment) {
		return isBaseTenant(segment);
	}

	public String normalizeToInternal(String tenantId) {
		if (isBaseTenant(tenantId)) {
			return getDefaultTenantId();
		}
		return tenantId;
	}

	public String normalizeCompositeToInternal(String composite) {
		if (composite == null || composite.isBlank()) {
			return composite;
		}
		String[] partes = composite.trim().split("/");
		if (partes.length == 0) {
			return composite;
		}
		if (isBaseTenant(partes[0].trim())) {
			if (partes.length == 1) {
				return getDefaultTenantId();
			}
			StringBuilder sb = new StringBuilder();
			for (int i = 1; i < partes.length; i++) {
				String p = partes[i] != null ? partes[i].trim() : "";
				if (p.isEmpty()) {
					continue;
				}
				if (sb.length() > 0) {
					sb.append("/");
				}
				sb.append(p);
			}
			String resto = sb.toString();
			return resto.isEmpty() ? getDefaultTenantId() : resto;
		}
		return composite.trim();
	}

	public void invalidateCache() {
		cachedBaseKey = null;
	}

	private String cargarLlaveOrganizacionPrincipal() {
		String url = env != null ? env.getProperty("db.url") : null;
		String user = env != null ? env.getProperty("db.username") : null;
		String pass = env != null ? env.getProperty("db.password") : null;
		String driver = env != null ? env.getProperty("db.driver") : null;
		if (url == null || url.isBlank()) {
			return null;
		}
		try {
			if (driver != null && !driver.isBlank()) {
				Class.forName(driver.trim());
			}
		} catch (Exception e) {
			return null;
		}
		String sql = "select corg_llave from organizacion_orgp where corg_principal is null and corg_estado = 'A' limit 1";
		try (Connection conn = DriverManager.getConnection(url, user, pass);
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {
			if (rs.next()) {
				String llave = rs.getString(1);
				if (llave != null && !llave.isBlank()) {
					return llave.trim();
				}
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}
}
