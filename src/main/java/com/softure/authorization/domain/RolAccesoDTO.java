package com.softure.authorization.domain;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.softure.java.domain.BasicDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
@Alias("RolAccesoDTO")
@JsonInclude(Include.NON_NULL)
public class RolAccesoDTO extends BasicDTO {

	private String plantilla;
	private String nombre;
	private String codigo;
	private String imagen;

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}

}