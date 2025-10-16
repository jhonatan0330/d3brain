package com.softure.tariff.domain;


import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("TarifaFilterDTO")
public class TarifaFilterDTO extends BasicFilterDTO
{

	private String tarifario;
	private String tarifarioNombre;
	private String documento;
	private String producto;
	private String productoDocumento;
	private String productoNombre;
	private String recurso;
	private String recursoNombre;
	private Integer cantidadMinima;
	private Integer cantidadMaxima;
	private String dimension2;
	private String dimension2Nombre;
	private String dimension3;
	private String dimension3Nombre;
	private String dimension4;
	private String dimension4Nombre;

	
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
	
	
	public void setDimension2(String dimension2) {
		this.dimension2 = dimension2;
	}
	
	public String getDimension2() {
		return dimension2;
	}
	
	
	public void setDimension2Nombre(String dimension2Nombre) {
		this.dimension2Nombre = dimension2Nombre;
	}
	
	public String getDimension2Nombre() {
		return dimension2Nombre;
	}
	
	
	public void setDimension3(String dimension3) {
		this.dimension3 = dimension3;
	}
	
	public String getDimension3() {
		return dimension3;
	}
	
	
	public void setDimension3Nombre(String dimension3Nombre) {
		this.dimension3Nombre = dimension3Nombre;
	}
	
	public String getDimension3Nombre() {
		return dimension3Nombre;
	}
	
	
	public void setDimension4(String dimension4) {
		this.dimension4 = dimension4;
	}
	
	public String getDimension4() {
		return dimension4;
	}
	
	
	public void setDimension4Nombre(String dimension4Nombre) {
		this.dimension4Nombre = dimension4Nombre;
	}
	
	public String getDimension4Nombre() {
		return dimension4Nombre;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getProductoDocumento() {
		return productoDocumento;
	}

	public void setProductoDocumento(String productoDocumento) {
		this.productoDocumento = productoDocumento;
	}
	

}