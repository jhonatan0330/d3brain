package com.softure.logisticpymes.domain.dto;

import java.math.BigDecimal;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("ProductoInventarioDescuentoDTO")
public class ProductoInventarioDescuentoDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String producto;
	private String productoDescontar;
	private String productoDescontarNombre;
	private BigDecimal cantidadProductoDescontar;
	private String caracteristica;
	private String caracteristicaNombre;

	public void setProducto(String producto) {
		this.producto = producto;
	}
	
	public String getProducto() {
		return producto;
	}
	public void setProductoDescontar(String productoDescontar) {
		this.productoDescontar = productoDescontar;
	}
	
	public String getProductoDescontar() {
		return productoDescontar;
	}
	public void setProductoDescontarNombre(String productoDescontarNombre) {
		this.productoDescontarNombre = productoDescontarNombre;
	}
	
	public String getProductoDescontarNombre() {
		return productoDescontarNombre;
	}
	public void setCantidadProductoDescontar(BigDecimal cantidadProductoDescontar) {
		this.cantidadProductoDescontar = cantidadProductoDescontar;
	}
	
	public BigDecimal getCantidadProductoDescontar() {
		return cantidadProductoDescontar;
	}
	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}
	
	public String getCaracteristica() {
		return caracteristica;
	}
	public void setCaracteristicaNombre(String caracteristicaNombre) {
		this.caracteristicaNombre = caracteristicaNombre;
	}
	
	public String getCaracteristicaNombre() {
		return caracteristicaNombre;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}