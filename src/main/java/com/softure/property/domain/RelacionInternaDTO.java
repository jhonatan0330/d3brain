package com.softure.property.domain;

import java.util.Date;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.softure.java.domain.BasicDTO;

@Alias("RelacionInternaDTO")
@JsonInclude(Include.NON_NULL)
public class RelacionInternaDTO extends BasicDTO
{

	private String propiedad;
	private String propiedadNombre;
	private String plantilla;
	private String plantillaNombre;
	private String campo;
	private String campoNombre;
	private String auxiliar;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicio;
	private String usuarioCreacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEliminacion;
	private String usuarioEliminacion;

	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}
	
	public String getPropiedad() {
		return propiedad;
	}
	public void setPropiedadNombre(String propiedadNombre) {
		this.propiedadNombre = propiedadNombre;
	}
	
	public String getPropiedadNombre() {
		return propiedadNombre;
	}
	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}
	
	public String getPlantilla() {
		return plantilla;
	}
	public void setPlantillaNombre(String plantillaNombre) {
		this.plantillaNombre = plantillaNombre;
	}
	
	public String getPlantillaNombre() {
		return plantillaNombre;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	
	public String getCampo() {
		return campo;
	}
	public void setCampoNombre(String campoNombre) {
		this.campoNombre = campoNombre;
	}
	
	public String getCampoNombre() {
		return campoNombre;
	}
	public void setAuxiliar(String auxiliar) {
		this.auxiliar = auxiliar;
	}
	
	public String getAuxiliar() {
		return auxiliar;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public Date getFechaEliminacion() {
		return fechaEliminacion;
	}

	public void setFechaEliminacion(Date fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}

	public String getUsuarioEliminacion() {
		return usuarioEliminacion;
	}

	public void setUsuarioEliminacion(String usuarioEliminacion) {
		this.usuarioEliminacion = usuarioEliminacion;
	}
	

}