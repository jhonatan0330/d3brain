package com.softure.authorization.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("ModuloDTO")
public class ModuloDTO extends BasicDTO
{

	private String nombre;
	private String moduloUrl;
	private String descripcion;
	private boolean movil;
	private String imagen;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setModuloUrl(String moduloUrl) {
		this.moduloUrl = moduloUrl;
	}
	
	public String getModuloUrl() {
		return moduloUrl;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setMovil(boolean movil) {
		this.movil = movil;
	}
	
	public boolean getMovil() {
		return movil;
	}

	public String getImagen() {
		return imagen;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

}