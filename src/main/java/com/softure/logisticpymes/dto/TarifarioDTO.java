package com.softure.logisticpymes.dto;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("TarifarioDTO")
public class TarifarioDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String nombre;
	private String tipoRecurso;
	private boolean productoOpcional;
	private boolean rangoValores;
	private String tipoRecursoNombre;
	private boolean rangoCantidad;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}
	
	public String getTipoRecurso() {
		return tipoRecurso;
	}
	public void setProductoOpcional(boolean productoOpcional) {
		this.productoOpcional = productoOpcional;
	}
	
	public boolean getProductoOpcional() {
		return productoOpcional;
	}
	public void setRangoValores(boolean rangoValores) {
		this.rangoValores = rangoValores;
	}
	
	public boolean getRangoValores() {
		return rangoValores;
	}
	public void setTipoRecursoNombre(String tipoRecursoNombre) {
		this.tipoRecursoNombre = tipoRecursoNombre;
	}
	
	public String getTipoRecursoNombre() {
		return tipoRecursoNombre;
	}
	public void setRangoCantidad(boolean rangoCantidad) {
		this.rangoCantidad = rangoCantidad;
	}
	
	public boolean getRangoCantidad() {
		return rangoCantidad;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}