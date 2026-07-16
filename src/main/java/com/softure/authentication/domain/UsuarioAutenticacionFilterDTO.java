package com.softure.authentication.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("UsuarioAutenticacionFilterDTO")
public class UsuarioAutenticacionFilterDTO extends BasicFilterDTO {

	private String usuario;
	private String sesion;
	private String clave;
	private String usuarioNombre;
	private String claveAnterior;
	private Integer tableroControl;
	private String mensaje;
	private String token;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMaximaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMaximaMax;
	private String ip;
	private String autorizacionCrea;
	private String autorizacionElimina;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaCreacionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaCreacionMax;

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

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

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setClaveAnterior(String claveAnterior) {
		this.claveAnterior = claveAnterior;
	}

	public String getClaveAnterior() {
		return claveAnterior;
	}

	public void setTableroControl(Integer tableroControl) {
		this.tableroControl = tableroControl;
	}

	public Integer getTableroControl() {
		return tableroControl;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getToken() {
		return token;
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

	public void setFechaCreacionMin(Date fechaCreacionMin) {
		this.fechaCreacionMin = fechaCreacionMin;
	}

	public Date getFechaCreacionMin() {
		return fechaCreacionMin;
	}

	public void setFechaCreacionMax(Date fechaCreacionMax) {
		this.fechaCreacionMax = fechaCreacionMax;
	}

	public Date getFechaCreacionMax() {
		return fechaCreacionMax;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIp() {
		return ip;
	}

	public void setAutorizacionCrea(String autorizacionCrea) {
		this.autorizacionCrea = autorizacionCrea;
	}

	public String getAutorizacionCrea() {
		return autorizacionCrea;
	}

	public void setAutorizacionElimina(String autorizacionElimina) {
		this.autorizacionElimina = autorizacionElimina;
	}

	public String getAutorizacionElimina() {
		return autorizacionElimina;
	}

}