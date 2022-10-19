package com.softure.logisticpymes.domain.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;
@Alias("CambioFilterDTO")
public class CambioFilterDTO extends BasicFilterDTO
{

	private String nombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaAplicacionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaAplicacionMax;
	private String sesionActiva;

	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
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
	
	
	public void setFechaAplicacionMin(Date fechaAplicacionMin) {
		this.fechaAplicacionMin = fechaAplicacionMin;
	}
	
	public Date getFechaAplicacionMin() {
		return fechaAplicacionMin;
	}
	
	public void setFechaAplicacionMax(Date fechaAplicacionMax) {
		this.fechaAplicacionMax = fechaAplicacionMax;
	}
	
	public Date getFechaAplicacionMax() {
		return fechaAplicacionMax;
	}
	
	
	public void setSesionActiva(String sesionActiva) {
		this.sesionActiva = sesionActiva;
	}
	
	public String getSesionActiva() {
		return sesionActiva;
	}
	

}