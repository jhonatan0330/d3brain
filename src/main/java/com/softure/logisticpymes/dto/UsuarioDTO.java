package com.softure.logisticpymes.dto;

import java.util.List;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("UsuarioDTO")
public class UsuarioDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String identificacion;
	private String nombre;
	private String imagen;
	private String rol;
	private String documento;
	private List<ProductoDTO> productos;
	private String usuarioFiltroDependiente;
	private String correo;
	private String usuarioRol;

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
	public void setProductos(List<ProductoDTO> productos) {
		this.productos = productos;
	}
	
	public List<ProductoDTO> getProductos() {
		return productos;
	}
	public void setUsuarioFiltroDependiente(String usuarioFiltroDependiente) {
		this.usuarioFiltroDependiente = usuarioFiltroDependiente;
	}
	
	public String getUsuarioFiltroDependiente() {
		return usuarioFiltroDependiente;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	
	public String getCorreo() {
		return correo;
	}
	public void setUsuarioRol(String usuarioRol) {
		this.usuarioRol = usuarioRol;
	}
	
	public String getUsuarioRol() {
		return usuarioRol;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}