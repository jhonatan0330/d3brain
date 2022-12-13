package com.softure.authorization.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("ModuloContratadoFilterDTO")
public class ModuloContratadoFilterDTO extends BasicFilterDTO
{

	private String modulo;
	private String nombre;
	private String imagen;
	private String moduloUrl;
	private String moduloLlave;

	
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	
	public String getModulo() {
		return modulo;
	}
	
	
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
	
	
	public void setModuloUrl(String moduloUrl) {
		this.moduloUrl = moduloUrl;
	}
	
	public String getModuloUrl() {
		return moduloUrl;
	}
	
	
	public void setModuloLlave(String moduloLlave) {
		this.moduloLlave = moduloLlave;
	}
	
	public String getModuloLlave() {
		return moduloLlave;
	}
	

}