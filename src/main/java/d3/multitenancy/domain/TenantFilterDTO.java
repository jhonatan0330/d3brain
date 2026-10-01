package d3.multitenancy.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObjectFilter;

@Alias("TenantFilterDTO")
public class TenantFilterDTO extends SharedDataObjectFilter {

	private String name;
	private String codigo;
	private String driver;
	private String datasourceUrl;
	private String datasourceUsername;
	private String datasourcePassword;
	private Date fechaValidez;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public Date getFechaValidez() {
		return fechaValidez;
	}

	public void setFechaValidez(Date fechaValidez) {
		this.fechaValidez = fechaValidez;
	}

}
