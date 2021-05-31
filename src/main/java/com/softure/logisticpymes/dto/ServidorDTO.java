package com.softure.logisticpymes.dto;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("ServidorDTO")
public class ServidorDTO extends BasicParamDTO
// BEGIN region interfaces  
// END region interfaces
{
	public static final String FTP = "F";
	public static final String WEB_SERVER = "W";
	public static final String BASE_DATOS = "B";
	public static final String MAIL = "E";
	public static final String LOCAL_FTP = "L";

	private String tipo;
	private Integer orden;
	private String nombre;
	private String url;
	private String puerto;
	private String usuario;
	private String clave;
	private String base;
	private String urlConexion;
	private String servidorRespaldo;

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	
	public Integer getOrden() {
		return orden;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}
	
	public String getPuerto() {
		return puerto;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	
	public String getClave() {
		return clave;
	}
	public void setBase(String base) {
		this.base = base;
	}
	
	public String getBase() {
		return base;
	}
	public void setUrlConexion(String urlConexion) {
		this.urlConexion = urlConexion;
	}
	
	public String getUrlConexion() {
		return urlConexion;
	}
	public void setServidorRespaldo(String servidorRespaldo) {
		this.servidorRespaldo = servidorRespaldo;
	}
	
	public String getServidorRespaldo() {
		return servidorRespaldo;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}