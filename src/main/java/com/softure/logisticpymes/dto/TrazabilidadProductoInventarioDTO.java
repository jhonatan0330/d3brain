package com.softure.logisticpymes.dto;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("TrazabilidadProductoInventarioDTO")
public class TrazabilidadProductoInventarioDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String bodega;
	private String producto;
	private String productoNombre;
	private BigDecimal cantidadInicial;
	private BigDecimal cantidadFinal;
	private BigDecimal cantidad;
	private String deduccionProducto;
	private String responsable;

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setBodega(String bodega) {
		this.bodega = bodega;
	}
	
	public String getBodega() {
		return bodega;
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
	public void setCantidadInicial(BigDecimal cantidadInicial) {
		this.cantidadInicial = cantidadInicial;
	}
	
	public BigDecimal getCantidadInicial() {
		return cantidadInicial;
	}
	public void setCantidadFinal(BigDecimal cantidadFinal) {
		this.cantidadFinal = cantidadFinal;
	}
	
	public BigDecimal getCantidadFinal() {
		return cantidadFinal;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setDeduccionProducto(String deduccionProducto) {
		this.deduccionProducto = deduccionProducto;
	}
	
	public String getDeduccionProducto() {
		return deduccionProducto;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
	public String getResponsable() {
		return responsable;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}