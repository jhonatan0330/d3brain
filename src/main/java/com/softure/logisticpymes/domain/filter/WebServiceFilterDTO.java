package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("WebServiceFilterDTO")
public class WebServiceFilterDTO extends BasicFilterDTO
{

	private String nombre;
	private String servidor;
	private String servidorNombre;

	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setServidor(String servidor) {
		this.servidor = servidor;
	}
	
	public String getServidor() {
		return servidor;
	}
	
	
	public void setServidorNombre(String servidorNombre) {
		this.servidorNombre = servidorNombre;
	}
	
	public String getServidorNombre() {
		return servidorNombre;
	}
	

}