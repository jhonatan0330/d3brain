package com.softure.webservice.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.logisticpymes.domain.BasicParamDTO;

@Alias("WebServiceDTO")
public class WebServiceDTO extends BasicParamDTO
{

	private String nombre;
	private String codigo;
	private String template;
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
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}