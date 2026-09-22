package d3.multitenancy.domain;

/**
 * Resultado de {@code GET /multitenancy/resolve}: el composite de tenant
 * (ej: "bytec/pioexpress") y el resto del path que corresponde a la ruta de la
 * aplicación SPA (ej: "/main").
 */
public class TenantResolveDTO {

	private String tenantId;
	private String rest;

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getRest() {
		return rest;
	}

	public void setRest(String rest) {
		this.rest = rest;
	}
}