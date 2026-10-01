package d3.usage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TenantKeyRequest {

	private String tenantKey;

	public void setTenantKey(String tenantKey) {
		this.tenantKey = tenantKey;
	}

	public String getTenantKey() {
		return tenantKey;
	}

}
