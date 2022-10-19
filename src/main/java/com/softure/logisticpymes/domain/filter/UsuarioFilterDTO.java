package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("UsuarioFilterDTO")
public class UsuarioFilterDTO extends BasicFilterDTO
{

	private String identificacion;
	private String nombre;
	private String imagen;
	private String rol;
	private String documento;
	private String usuarioFiltroDependiente;
	private String usuarioRol;
	private String telefono;

	
	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}
	
	public String getIdentificacion() {
		return identificacion;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	
	public void setRol(String rol) {
		this.rol = rol;
	}
	
	public String getRol() {
		return rol;
	}
	
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	
					
	
	public void setUsuarioFiltroDependiente(String usuarioFiltroDependiente) {
		this.usuarioFiltroDependiente = usuarioFiltroDependiente;
	}
	
	public String getUsuarioFiltroDependiente() {
		return usuarioFiltroDependiente;
	}
	
	
	public void setUsuarioRol(String usuarioRol) {
		this.usuarioRol = usuarioRol;
	}
	
	public String getUsuarioRol() {
		return usuarioRol;
	}
	
	
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	public String getTelefono() {
		return telefono;
	}
	

}