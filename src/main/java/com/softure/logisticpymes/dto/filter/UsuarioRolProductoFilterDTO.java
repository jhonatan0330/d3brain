package com.softure.logisticpymes.dto.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("UsuarioRolProductoFilterDTO")
public class UsuarioRolProductoFilterDTO extends BasicFilterDTO
{

	private String documento;
	private String documentoNombre;
	private String producto;
	private String productoNombre;
	private String nombre;
	private Integer cantidadPromocion;
	private Integer cantidadPromocionBase;

	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	
	
	public void setDocumentoNombre(String documentoNombre) {
		this.documentoNombre = documentoNombre;
	}
	
	public String getDocumentoNombre() {
		return documentoNombre;
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
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setCantidadPromocion(Integer cantidadPromocion) {
		this.cantidadPromocion = cantidadPromocion;
	}
	
	public Integer getCantidadPromocion() {
		return cantidadPromocion;
	}
	
	
	public void setCantidadPromocionBase(Integer cantidadPromocionBase) {
		this.cantidadPromocionBase = cantidadPromocionBase;
	}
	
	public Integer getCantidadPromocionBase() {
		return cantidadPromocionBase;
	}
	

}