package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("ModuloFilterDTO")
public class ModuloFilterDTO extends BasicFilterDTO
{

	private String nombre;
	private String url;
 	private Boolean movilFilter = null;

	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
 	
 	public void setMovilFilter(Boolean movilFilter) {
		this.movilFilter = movilFilter;
	}
	
	public Boolean getMovilFilter() {
		return movilFilter;
	}
	

}