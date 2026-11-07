package d3.multitenancy.domain;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObject;

@Alias("TenantDTO")
public class TenantDTO extends SharedDataObject {

	private String name;
	private String codigo;
	private String driver;
	private String datasourceUrl;
	private String datasourceUsername;
	private String datasourcePassword;
	private String imagen;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate fechaBloqueo;

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

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public LocalDate getFechaBloqueo() {
		return fechaBloqueo;
	}

	public void setFechaBloqueo(LocalDate fechaBloqueo) {
		this.fechaBloqueo = fechaBloqueo;
	}

}
