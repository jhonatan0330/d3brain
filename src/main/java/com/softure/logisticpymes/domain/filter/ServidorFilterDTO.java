package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("ServidorFilterDTO")
public class ServidorFilterDTO extends BasicFilterDTO
{

	private String tipo;
	private Integer orden;
	private String nombre;
	private String puerto;
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
	
	
	public void setPuerto(String puerto) {
		this.puerto = puerto;
	}
	
	public String getPuerto() {
		return puerto;
	}
	
	
	public void setServidorRespaldo(String servidorRespaldo) {
		this.servidorRespaldo = servidorRespaldo;
	}
	
	public String getServidorRespaldo() {
		return servidorRespaldo;
	}
	

}