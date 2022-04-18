package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("TransaccionErrorFilterDTO")
public class TransaccionErrorFilterDTO extends BasicFilterDTO
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMax;
	private String usuario;

	
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
	
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	

}