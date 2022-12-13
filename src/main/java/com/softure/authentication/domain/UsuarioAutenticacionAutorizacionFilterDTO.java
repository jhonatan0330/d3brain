package com.softure.authentication.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;
@Alias("UsuarioAutenticacionAutorizacionFilterDTO")
public class UsuarioAutenticacionAutorizacionFilterDTO extends BasicFilterDTO
{

	private String usuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMaximaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMaximaMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaSolicitudMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaSolicitudMax;
	private String correo;
	private String ipSolicitud;
	private String codigo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRedencionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRedencionMax;
	private String key;
	private String ipRedencion;

	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	
	public void setFechaMaximaMin(Date fechaMaximaMin) {
		this.fechaMaximaMin = fechaMaximaMin;
	}
	
	public Date getFechaMaximaMin() {
		return fechaMaximaMin;
	}
	
	public void setFechaMaximaMax(Date fechaMaximaMax) {
		this.fechaMaximaMax = fechaMaximaMax;
	}
	
	public Date getFechaMaximaMax() {
		return fechaMaximaMax;
	}
	
	
	public void setFechaSolicitudMin(Date fechaSolicitudMin) {
		this.fechaSolicitudMin = fechaSolicitudMin;
	}
	
	public Date getFechaSolicitudMin() {
		return fechaSolicitudMin;
	}
	
	public void setFechaSolicitudMax(Date fechaSolicitudMax) {
		this.fechaSolicitudMax = fechaSolicitudMax;
	}
	
	public Date getFechaSolicitudMax() {
		return fechaSolicitudMax;
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
	
	
	public void setFechaRedencionMin(Date fechaRedencionMin) {
		this.fechaRedencionMin = fechaRedencionMin;
	}
	
	public Date getFechaRedencionMin() {
		return fechaRedencionMin;
	}
	
	public void setFechaRedencionMax(Date fechaRedencionMax) {
		this.fechaRedencionMax = fechaRedencionMax;
	}
	
	public Date getFechaRedencionMax() {
		return fechaRedencionMax;
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
	

}