package d3.usage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MovimientoHijoRequest {

	private String tenantKey;
	private MovimientoConsumoFilterDTO filter;

	public void setTenantKey(String tenantKey) {
		this.tenantKey = tenantKey;
	}

	public String getTenantKey() {
		return tenantKey;
	}

	public void setFilter(MovimientoConsumoFilterDTO filter) {
		this.filter = filter;
	}

	public MovimientoConsumoFilterDTO getFilter() {
		return filter;
	}

}
