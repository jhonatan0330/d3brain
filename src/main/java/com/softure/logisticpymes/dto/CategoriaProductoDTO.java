package com.softure.logisticpymes.dto;

import java.util.List;

import java.math.BigDecimal;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("CategoriaProductoDTO")
public class CategoriaProductoDTO extends BasicParamDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String nombre;
	private String imagen;
	private BigDecimal cantidadMaxima;
	private String nodoSuperior;
	private List<CategoriaProductoDTO> hijos;
	private boolean inventarios;
	private boolean camposAdicionales;
	private boolean composicion;
	private Integer promocionBase;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	public void setCantidadMaxima(BigDecimal cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}
	
	public BigDecimal getCantidadMaxima() {
		return cantidadMaxima;
	}
	public void setNodoSuperior(String nodoSuperior) {
		this.nodoSuperior = nodoSuperior;
	}
	
	public String getNodoSuperior() {
		return nodoSuperior;
	}
	public void setHijos(List<CategoriaProductoDTO> hijos) {
		this.hijos = hijos;
	}
	
	public List<CategoriaProductoDTO> getHijos() {
		return hijos;
	}
	public void setInventarios(boolean inventarios) {
		this.inventarios = inventarios;
	}
	
	public boolean getInventarios() {
		return inventarios;
	}
	public void setCamposAdicionales(boolean camposAdicionales) {
		this.camposAdicionales = camposAdicionales;
	}
	
	public boolean getCamposAdicionales() {
		return camposAdicionales;
	}
	public void setComposicion(boolean composicion) {
		this.composicion = composicion;
	}
	
	public boolean getComposicion() {
		return composicion;
	}
	public void setPromocionBase(Integer promocionBase) {
		this.promocionBase = promocionBase;
	}
	
	public Integer getPromocionBase() {
		return promocionBase;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}