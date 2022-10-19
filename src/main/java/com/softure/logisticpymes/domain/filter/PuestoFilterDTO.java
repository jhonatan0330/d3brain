package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("PuestoFilterDTO")
public class PuestoFilterDTO extends BasicFilterDTO
{

	private String campo;
	private Integer fila;
	private Integer columna;
	private String imagen;
	private String nombre;

	
	public void setCampo(String campo) {
		this.campo = campo;
	}
	
	public String getCampo() {
		return campo;
	}
	
	
	public void setFila(Integer fila) {
		this.fila = fila;
	}
	
	public Integer getFila() {
		return fila;
	}
	
	
	public void setColumna(Integer columna) {
		this.columna = columna;
	}
	
	public Integer getColumna() {
		return columna;
	}
	
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	

}