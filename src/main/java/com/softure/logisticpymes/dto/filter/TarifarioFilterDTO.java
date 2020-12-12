package com.softure.logisticpymes.dto.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("TarifarioFilterDTO")
public class TarifarioFilterDTO extends BasicFilterDTO
{

	private String nombre;
	private String tipoRecurso;
 	private Boolean productoOpcionalFilter = null;
 	private Boolean rangoValoresFilter = null;
	private String tipoRecursoNombre;
 	private Boolean rangoCantidadFilter = null;

	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setTipoRecurso(String tipoRecurso) {
		this.tipoRecurso = tipoRecurso;
	}
	
	public String getTipoRecurso() {
		return tipoRecurso;
	}
	
 	
 	public void setProductoOpcionalFilter(Boolean productoOpcionalFilter) {
		this.productoOpcionalFilter = productoOpcionalFilter;
	}
	
	public Boolean getProductoOpcionalFilter() {
		return productoOpcionalFilter;
	}
	
 	
 	public void setRangoValoresFilter(Boolean rangoValoresFilter) {
		this.rangoValoresFilter = rangoValoresFilter;
	}
	
	public Boolean getRangoValoresFilter() {
		return rangoValoresFilter;
	}
	
	
	public void setTipoRecursoNombre(String tipoRecursoNombre) {
		this.tipoRecursoNombre = tipoRecursoNombre;
	}
	
	public String getTipoRecursoNombre() {
		return tipoRecursoNombre;
	}
	
 	
 	public void setRangoCantidadFilter(Boolean rangoCantidadFilter) {
		this.rangoCantidadFilter = rangoCantidadFilter;
	}
	
	public Boolean getRangoCantidadFilter() {
		return rangoCantidadFilter;
	}
	

}