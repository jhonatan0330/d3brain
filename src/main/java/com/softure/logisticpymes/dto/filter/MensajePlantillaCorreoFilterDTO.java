package com.softure.logisticpymes.dto.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("MensajePlantillaCorreoFilterDTO")
public class MensajePlantillaCorreoFilterDTO extends BasicFilterDTO
{

	private String nombre;
	private String servidor;

	
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
	

}