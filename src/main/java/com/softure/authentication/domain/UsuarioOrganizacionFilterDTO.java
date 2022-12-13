package com.softure.authentication.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("UsuarioOrganizacionFilterDTO")
public class UsuarioOrganizacionFilterDTO extends BasicFilterDTO
{

	private String usuario;
	private String organizacion;
	private String tokenServer;
	private String usuarioNombre;

	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	
	public void setOrganizacion(String organizacion) {
		this.organizacion = organizacion;
	}
	
	public String getOrganizacion() {
		return organizacion;
	}
	
	
	public void setTokenServer(String tokenServer) {
		this.tokenServer = tokenServer;
	}
	
	public String getTokenServer() {
		return tokenServer;
	}
	
	
	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}
	
	public String getUsuarioNombre() {
		return usuarioNombre;
	}
	

}