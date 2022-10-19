package com.softure.logisticpymes.domain.dto;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("ProductoInventarioDTO")
public class ProductoInventarioDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String producto;
	private String nombre;
	private String codigo;
	private String bodega;
	private String nombreBodega;
	private BigDecimal cantidadActual;
	private BigDecimal cantidadMinima;
	private BigDecimal cantidadMaxima;
	private BigDecimal cantidadModificar;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicial;

	public void setProducto(String producto) {
		this.producto = producto;
	}
	
	public String getProducto() {
		return producto;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setBodega(String bodega) {
		this.bodega = bodega;
	}
	
	public String getBodega() {
		return bodega;
	}
	public void setNombreBodega(String nombreBodega) {
		this.nombreBodega = nombreBodega;
	}
	
	public String getNombreBodega() {
		return nombreBodega;
	}
	public void setCantidadActual(BigDecimal cantidadActual) {
		this.cantidadActual = cantidadActual;
	}
	
	public BigDecimal getCantidadActual() {
		return cantidadActual;
	}
	public void setCantidadMinima(BigDecimal cantidadMinima) {
		this.cantidadMinima = cantidadMinima;
	}
	
	public BigDecimal getCantidadMinima() {
		return cantidadMinima;
	}
	public void setCantidadMaxima(BigDecimal cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}
	
	public BigDecimal getCantidadMaxima() {
		return cantidadMaxima;
	}
	public void setCantidadModificar(BigDecimal cantidadModificar) {
		this.cantidadModificar = cantidadModificar;
	}
	
	public BigDecimal getCantidadModificar() {
		return cantidadModificar;
	}
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	
	public Date getFechaInicial() {
		return fechaInicial;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}