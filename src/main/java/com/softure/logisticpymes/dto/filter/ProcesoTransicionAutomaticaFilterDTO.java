package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("ProcesoTransicionAutomaticaFilterDTO")
public class ProcesoTransicionAutomaticaFilterDTO extends BasicFilterDTO
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String transicion;
	private String plantilla;
	private String plantillaNombre;
	private String propiedad;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date ejecucionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date ejecucionMax;

	
	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}
	
	public Date getFechaMin() {
		return fechaMin;
	}
	
	public void setFechaMax(Date fechaMax) {
		this.fechaMax = fechaMax;
	}
	
	public Date getFechaMax() {
		return fechaMax;
	}
	
	
	public void setTransicion(String transicion) {
		this.transicion = transicion;
	}
	
	public String getTransicion() {
		return transicion;
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
	
	
	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}
	
	public String getPropiedad() {
		return propiedad;
	}
	
	
	public void setEjecucionMin(Date ejecucionMin) {
		this.ejecucionMin = ejecucionMin;
	}
	
	public Date getEjecucionMin() {
		return ejecucionMin;
	}
	
	public void setEjecucionMax(Date ejecucionMax) {
		this.ejecucionMax = ejecucionMax;
	}
	
	public Date getEjecucionMax() {
		return ejecucionMax;
	}
	

}