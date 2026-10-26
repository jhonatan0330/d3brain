package d3.authentication.domain;


import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicFilterDTO;

@Alias("OrganizacionFilterDTO")
public class OrganizacionFilterDTO extends BasicFilterDTO {

	private String nombre;
	private String imagen;
	private String codigo;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
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