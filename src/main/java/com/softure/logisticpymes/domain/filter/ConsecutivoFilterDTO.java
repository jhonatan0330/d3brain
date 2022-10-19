package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("ConsecutivoFilterDTO")
public class ConsecutivoFilterDTO extends BasicFilterDTO
{

	private String nombre;
	private String prefijo;
	private String sufijo;
 	private Boolean manualFilter = null;
	private String padding;
	private String consecutivoActual;

	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
	
	public String getPrefijo() {
		return prefijo;
	}
	
	
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	
	public String getSufijo() {
		return sufijo;
	}
	
 	
 	public void setManualFilter(Boolean manualFilter) {
		this.manualFilter = manualFilter;
	}
	
	public Boolean getManualFilter() {
		return manualFilter;
	}
	
	
	public void setPadding(String padding) {
		this.padding = padding;
	}
	
	public String getPadding() {
		return padding;
	}
	
	
	public void setConsecutivoActual(String consecutivoActual) {
		this.consecutivoActual = consecutivoActual;
	}
	
	public String getConsecutivoActual() {
		return consecutivoActual;
	}
	

}