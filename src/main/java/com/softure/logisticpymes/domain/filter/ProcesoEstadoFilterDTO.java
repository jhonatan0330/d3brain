package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("ProcesoEstadoFilterDTO")
public class ProcesoEstadoFilterDTO extends BasicFilterDTO
{

	private String tipo;
	private String estadoDocumento;
	private Integer avance;
	private String nombre;
	private String proceso;
	private String procesoNombre;

	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	
	
	public void setEstadoDocumento(String estadoDocumento) {
		this.estadoDocumento = estadoDocumento;
	}
	
	public String getEstadoDocumento() {
		return estadoDocumento;
	}
	
	
	public void setAvance(Integer avance) {
		this.avance = avance;
	}
	
	public Integer getAvance() {
		return avance;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setProceso(String proceso) {
		this.proceso = proceso;
	}
	
	public String getProceso() {
		return proceso;
	}
	
	
	public void setProcesoNombre(String procesoNombre) {
		this.procesoNombre = procesoNombre;
	}
	
	public String getProcesoNombre() {
		return procesoNombre;
	}
	
					

}