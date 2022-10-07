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
	private String tipoRecursoNombre;
	private String tipoDimension2;
	private String tipoDimension2Nombre;
	private String tipoDimension3;
	private String tipoDimension3Nombre;
	private String tipoDimension4;
	private String tipoDimension4Nombre;
	private boolean productoOpcional;
	private boolean rangoValores;
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
	public void setTipoRecursoNombre(String tipoRecursoNombre) {
		this.tipoRecursoNombre = tipoRecursoNombre;
	}
	
	public String getTipoRecursoNombre() {
		return tipoRecursoNombre;
	}
	public void setTipoDimension2(String tipoDimension2) {
		this.tipoDimension2 = tipoDimension2;
	}
	
	public String getTipoDimension2() {
		return tipoDimension2;
	}
	public void setTipoDimension2Nombre(String tipoDimension2Nombre) {
		this.tipoDimension2Nombre = tipoDimension2Nombre;
	}
	
	public String getTipoDimension2Nombre() {
		return tipoDimension2Nombre;
	}
	public void setTipoDimension3(String tipoDimension3) {
		this.tipoDimension3 = tipoDimension3;
	}
	
	public String getTipoDimension3() {
		return tipoDimension3;
	}
	public void setTipoDimension3Nombre(String tipoDimension3Nombre) {
		this.tipoDimension3Nombre = tipoDimension3Nombre;
	}
	
	public String getTipoDimension3Nombre() {
		return tipoDimension3Nombre;
	}
	public void setTipoDimension4(String tipoDimension4) {
		this.tipoDimension4 = tipoDimension4;
	}
	
	public String getTipoDimension4() {
		return tipoDimension4;
	}
	public void setTipoDimension4Nombre(String tipoDimension4Nombre) {
		this.tipoDimension4Nombre = tipoDimension4Nombre;
	}
	
	public String getTipoDimension4Nombre() {
		return tipoDimension4Nombre;
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
	public void setRangoCantidad(boolean rangoCantidad) {
		this.rangoCantidad = rangoCantidad;
	}
	
	public boolean getRangoCantidad() {
		return rangoCantidad;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}