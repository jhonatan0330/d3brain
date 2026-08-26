package d3.multitenancy.domain;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObjectFilter;

@Alias("TenantFilterDTO")
public class TenantFilterDTO extends SharedDataObjectFilter {

	private String name;
	private String driver;
	private String datasourceUrl;
	private String datasourceUsername;
	private String datasourcePassword;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDatasourceUrl() {
		return datasourceUrl;
	}

	public void setDatasourceUrl(String datasourceUrl) {
		this.datasourceUrl = datasourceUrl;
	}

	public String getDatasourceUsername() {
		return datasourceUsername;
	}

	public void setDatasourceUsername(String datasourceUsername) {
		this.datasourceUsername = datasourceUsername;
	}

	public String getDatasourcePassword() {
		return datasourcePassword;
	}

	public void setDatasourcePassword(String datasourcePassword) {
		this.datasourcePassword = datasourcePassword;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

}
