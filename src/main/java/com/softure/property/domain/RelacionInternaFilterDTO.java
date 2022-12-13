package com.softure.property.domain;

import java.util.Date;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;
@Alias("RelacionInternaFilterDTO")
public class RelacionInternaFilterDTO extends BasicFilterDTO
{

	private String propiedad;
	private String propiedadNombre;
	private String plantilla;
	private String plantillaNombre;
	private String campo;
	private String campoNombre;
	private String auxiliar;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMax;
	private String cambioCreacion;
	private String cambioEliminacion;

	
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
	

	public void setFechaInicioMin(Date fechaInicioMin) {
		this.fechaInicioMin = fechaInicioMin;
	}
	
	public Date getFechaInicioMin() {
		return fechaInicioMin;
	}
	
	public void setFechaInicioMax(Date fechaInicioMax) {
		this.fechaInicioMax = fechaInicioMax;
	}
	
	public Date getFechaInicioMax() {
		return fechaInicioMax;
	}
	
	
	public void setCambioCreacion(String cambioCreacion) {
		this.cambioCreacion = cambioCreacion;
	}
	
	public String getCambioCreacion() {
		return cambioCreacion;
	}
	
	
	public void setCambioEliminacion(String cambioEliminacion) {
		this.cambioEliminacion = cambioEliminacion;
	}
	
	public String getCambioEliminacion() {
		return cambioEliminacion;
	}
	
}