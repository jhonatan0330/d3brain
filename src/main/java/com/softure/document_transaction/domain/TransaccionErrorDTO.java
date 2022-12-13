package com.softure.document_transaction.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("TransaccionErrorDTO")
public class TransaccionErrorDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFin;
	private String error;
	private String usuario;

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public String getError() {
		return error;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}