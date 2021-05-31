package com.softure.logisticpymes.dto;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("WebServiceDTO")
public class WebServiceDTO extends BasicParamDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String nombre;
	private String template;
	private String servidor;
	private String servidorNombre;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	
	public String getTemplate() {
		return template;
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
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}