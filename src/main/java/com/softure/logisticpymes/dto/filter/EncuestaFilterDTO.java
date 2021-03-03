package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("EncuestaFilterDTO")
public class EncuestaFilterDTO extends BasicFilterDTO
{

	private String nombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEjecucionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEjecucionMax;
 	private Boolean colaborativaFilter = null;
	private String rol;
	private String cliente;

	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
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
	
	
	public void setFechaFinMin(Date fechaFinMin) {
		this.fechaFinMin = fechaFinMin;
	}
	
	public Date getFechaFinMin() {
		return fechaFinMin;
	}
	
	public void setFechaFinMax(Date fechaFinMax) {
		this.fechaFinMax = fechaFinMax;
	}
	
	public Date getFechaFinMax() {
		return fechaFinMax;
	}
	
	
	public void setFechaEjecucionMin(Date fechaEjecucionMin) {
		this.fechaEjecucionMin = fechaEjecucionMin;
	}
	
	public Date getFechaEjecucionMin() {
		return fechaEjecucionMin;
	}
	
	public void setFechaEjecucionMax(Date fechaEjecucionMax) {
		this.fechaEjecucionMax = fechaEjecucionMax;
	}
	
	public Date getFechaEjecucionMax() {
		return fechaEjecucionMax;
	}
	
 	
 	public void setColaborativaFilter(Boolean colaborativaFilter) {
		this.colaborativaFilter = colaborativaFilter;
	}
	
	public Boolean getColaborativaFilter() {
		return colaborativaFilter;
	}
	
	
	public void setRol(String rol) {
		this.rol = rol;
	}
	
	public String getRol() {
		return rol;
	}
	
	
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	public String getCliente() {
		return cliente;
	}
	
					

}