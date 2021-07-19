package com.softure.logisticpymes.dto.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("UsuarioAutenticacionFilterDTO")
public class UsuarioAutenticacionFilterDTO extends BasicFilterDTO
{

	private String usuario;
	private String sesion;
	private String clave;
	private String usuarioNombre;
	private String claveAnterior;
	private Integer tableroControl;
	private String mensaje;
	private String token;

	
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
	
					

}