package com.softure.process_designer.domain;


import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;

@Alias("ProcesoEstadoFilterDTO")
public class ProcesoEstadoFilterDTO extends BasicFilterDTO {

	private String tipo;
	private String estadoDocumento;
	private Integer avance;
	private String nombre;
	private String codigo;
	private String proceso;
	private String procesoNombre;

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEstadoDocumento() {
		return estadoDocumento;
	}

	public void setEstadoDocumento(String estadoDocumento) {
		this.estadoDocumento = estadoDocumento;
	}

	public Integer getAvance() {
		return avance;
	}

	public void setAvance(Integer avance) {
		this.avance = avance;
	}

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

	public String getProceso() {
		return proceso;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProcesoNombre() {
		return procesoNombre;
	}

	public void setProcesoNombre(String procesoNombre) {
		this.procesoNombre = procesoNombre;
	}

}