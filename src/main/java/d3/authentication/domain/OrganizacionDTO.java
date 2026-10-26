package d3.authentication.domain;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicParamDTO;

@Alias("OrganizacionDTO")
public class OrganizacionDTO extends BasicParamDTO {
	private String nombre;
	private String principal;
	
	private String imagen;
	private String codigo;
	

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getPrincipal() {
		return principal;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

}