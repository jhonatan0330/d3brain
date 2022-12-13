package com.softure.authentication.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("UsuarioSesionErrorDTO")
public class UsuarioSesionErrorDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String sesion;
	private String clave;
	private String ip;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String error;

	public void setSesion(String sesion) {
		this.sesion = sesion;
	}
	
	public String getSesion() {
		return sesion;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String getClave() {
		return clave;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getIp() {
		return ip;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public String getError() {
		return error;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}