package com.softure.authentication.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("UsuarioAutenticacionAutorizacionDTO")
public class UsuarioAutenticacionAutorizacionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String usuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMaxima;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaSolicitud;
	private String correo;
	private String ipSolicitud;
	private String codigo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRedencion;
	private String key;
	private String ipRedencion;

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setFechaMaxima(Date fechaMaxima) {
		this.fechaMaxima = fechaMaxima;
	}
	
	public Date getFechaMaxima() {
		return fechaMaxima;
	}
	public void setFechaSolicitud(Date fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	
	public Date getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public String getCorreo() {
		return correo;
	}
	public void setIpSolicitud(String ipSolicitud) {
		this.ipSolicitud = ipSolicitud;
	}
	
	public String getIpSolicitud() {
		return ipSolicitud;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setFechaRedencion(Date fechaRedencion) {
		this.fechaRedencion = fechaRedencion;
	}
	
	public Date getFechaRedencion() {
		return fechaRedencion;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	public void setIpRedencion(String ipRedencion) {
		this.ipRedencion = ipRedencion;
	}
	
	public String getIpRedencion() {
		return ipRedencion;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}