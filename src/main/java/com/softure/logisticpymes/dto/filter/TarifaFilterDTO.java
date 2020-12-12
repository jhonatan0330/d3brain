package com.softure.logisticpymes.dto.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("TarifaFilterDTO")
public class TarifaFilterDTO extends BasicFilterDTO
{

	private String tarifario;
	private String tarifarioNombre;
	private String producto;
	private String productoNombre;
	private String recurso;
	private String recursoNombre;
 	private Boolean rangoPreciosFilter = null;
	private Integer cantidadMinima;
	private Integer cantidadMaxima;

	
	public void setTarifario(String tarifario) {
		this.tarifario = tarifario;
	}
	
	public String getTarifario() {
		return tarifario;
	}
	
	
	public void setTarifarioNombre(String tarifarioNombre) {
		this.tarifarioNombre = tarifarioNombre;
	}
	
	public String getTarifarioNombre() {
		return tarifarioNombre;
	}
	
	
	public void setProducto(String producto) {
		this.producto = producto;
	}
	
	public String getProducto() {
		return producto;
	}
	
	
	public void setProductoNombre(String productoNombre) {
		this.productoNombre = productoNombre;
	}
	
	public String getProductoNombre() {
		return productoNombre;
	}
	
	
	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}
	
	public String getRecurso() {
		return recurso;
	}
	
	
	public void setRecursoNombre(String recursoNombre) {
		this.recursoNombre = recursoNombre;
	}
	
	public String getRecursoNombre() {
		return recursoNombre;
	}
	
 	
 	public void setRangoPreciosFilter(Boolean rangoPreciosFilter) {
		this.rangoPreciosFilter = rangoPreciosFilter;
	}
	
	public Boolean getRangoPreciosFilter() {
		return rangoPreciosFilter;
	}
	
	
	public void setCantidadMinima(Integer cantidadMinima) {
		this.cantidadMinima = cantidadMinima;
	}
	
	public Integer getCantidadMinima() {
		return cantidadMinima;
	}
	
	
	public void setCantidadMaxima(Integer cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}
	
	public Integer getCantidadMaxima() {
		return cantidadMaxima;
	}
	

}