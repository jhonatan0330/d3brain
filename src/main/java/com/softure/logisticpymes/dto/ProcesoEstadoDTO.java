package com.softure.logisticpymes.dto;

import java.util.List;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("ProcesoEstadoDTO")
public class ProcesoEstadoDTO extends BasicParamDTO
// BEGIN region interfaces  
// END region interfaces
{
	public static final String TIPO_ESTADO = "E";
	public static final String TIPO_DECISION = "D";
	public static final String TIPO_ITERADOR = "R";
	public static final String ACTIVO = "A";
	public static final String FINALIZADO = "C";
	public static final String INACTIVO = "I";

	private String tipo;
	private String estadoDocumento;
	private Integer avance;
	private String nombre;
	private String proceso;
	private String procesoNombre;
	private List<ProcesoTransicionDTO> transiciones;

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
	public void setTransiciones(List<ProcesoTransicionDTO> transiciones) {
		this.transiciones = transiciones;
	}
	
	public List<ProcesoTransicionDTO> getTransiciones() {
		return transiciones;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}