package com.softure.webservice.domain;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.softure.logisticpymes.domain.BasicParamDTO;

@JsonIgnoreProperties(ignoreUnknown = true)
@Alias("WebServiceDTO")
public class WebServiceDTO extends BasicParamDTO {

	private String nombre;
	private String codigo;
	private String proceso;

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

	/*
	 * public String getTemplate() { return template; } public void
	 * setTemplate(String template) { this.template = template; } public String
	 * getUrl() { return url; } public void setUrl(String url) { this.url = url; }
	 */
	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

}