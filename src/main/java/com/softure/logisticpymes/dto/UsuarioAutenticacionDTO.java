package com.softure.logisticpymes.dto;

import java.util.List;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("UsuarioAutenticacionDTO")
public class UsuarioAutenticacionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String usuario;
	private String sesion;
	private String clave;
	private String usuarioNombre;
	private String claveAnterior;
	private Integer tableroControl;
	private UsuarioDTO usuarioDTO;
	private List<OrganizacionDTO> organizaciones;
	private String mensaje;
	private String token;
	private List<ModuloContratadoDTO> modulos;

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
	public void setUsuarioDTO(UsuarioDTO usuarioDTO) {
		this.usuarioDTO = usuarioDTO;
	}
	
	public UsuarioDTO getUsuarioDTO() {
		return usuarioDTO;
	}
	public void setOrganizaciones(List<OrganizacionDTO> organizaciones) {
		this.organizaciones = organizaciones;
	}
	
	public List<OrganizacionDTO> getOrganizaciones() {
		return organizaciones;
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
	public void setModulos(List<ModuloContratadoDTO> modulos) {
		this.modulos = modulos;
	}
	
	public List<ModuloContratadoDTO> getModulos() {
		return modulos;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}