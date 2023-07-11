package com.softure.webservice.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;


@Alias("WebServiceFilterDTO")
public class WebServiceFilterDTO extends BasicFilterDTO
{

	private String nombre;
	private String codigo;
	private String url;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}