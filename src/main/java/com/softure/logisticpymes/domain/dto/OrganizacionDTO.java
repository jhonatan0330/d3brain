package com.softure.logisticpymes.domain.dto;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("OrganizacionDTO")
public class OrganizacionDTO extends BasicParamDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String nombre;
	private String principal;
	private String servidor;
	private String usuarioSystem;
	private String imagen;
	private String slogan;
	private boolean sincronizacion;
	private String mensajeIngreso;
	private String codigo;
	private String servidorUrl;
	private String servidorCorreo;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	public String getPrincipal() {
		return principal;
	}
	public void setServidor(String servidor) {
		this.servidor = servidor;
	}
	
	public String getServidor() {
		return servidor;
	}
	public void setUsuarioSystem(String usuarioSystem) {
		this.usuarioSystem = usuarioSystem;
	}
	
	public String getUsuarioSystem() {
		return usuarioSystem;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	
	public String getSlogan() {
		return slogan;
	}
	public void setSincronizacion(boolean sincronizacion) {
		this.sincronizacion = sincronizacion;
	}
	
	public boolean getSincronizacion() {
		return sincronizacion;
	}
	public void setMensajeIngreso(String mensajeIngreso) {
		this.mensajeIngreso = mensajeIngreso;
	}
	
	public String getMensajeIngreso() {
		return mensajeIngreso;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setServidorUrl(String servidorUrl) {
		this.servidorUrl = servidorUrl;
	}
	
	public String getServidorUrl() {
		return servidorUrl;
	}
	public void setServidorCorreo(String servidorCorreo) {
		this.servidorCorreo = servidorCorreo;
	}
	
	public String getServidorCorreo() {
		return servidorCorreo;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}