package com.softure.logisticpymes.dto;

import java.math.BigDecimal;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("TarifaDTO")
public class TarifaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String tarifario;
	private String tarifarioNombre;
	private String producto;
	private String productoNombre;
	private String recurso;
	private String recursoNombre;
	private boolean rangoPrecios;
	private BigDecimal valorMinimo;
	private BigDecimal valor;
	private BigDecimal valorMaximo;
	private Integer cantidadMinima;
	private Integer cantidadMaxima;
	private BigDecimal totalMinimo;

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
	public void setRangoPrecios(boolean rangoPrecios) {
		this.rangoPrecios = rangoPrecios;
	}
	
	public boolean getRangoPrecios() {
		return rangoPrecios;
	}
	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}
	
	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
	public void setValorMaximo(BigDecimal valorMaximo) {
		this.valorMaximo = valorMaximo;
	}
	
	public BigDecimal getValorMaximo() {
		return valorMaximo;
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
	public void setTotalMinimo(BigDecimal totalMinimo) {
		this.totalMinimo = totalMinimo;
	}
	
	public BigDecimal getTotalMinimo() {
		return totalMinimo;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}