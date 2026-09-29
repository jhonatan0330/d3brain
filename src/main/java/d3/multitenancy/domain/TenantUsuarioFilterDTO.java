package d3.multitenancy.domain;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObjectFilter;

@Alias("TenantUsuarioFilterDTO")
public class TenantUsuarioFilterDTO extends SharedDataObjectFilter {

	private String usuario;

	private String tenant;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
}
