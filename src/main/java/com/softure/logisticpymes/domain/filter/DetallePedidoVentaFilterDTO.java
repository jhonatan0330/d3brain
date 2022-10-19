package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("DetallePedidoVentaFilterDTO")
public class DetallePedidoVentaFilterDTO extends BasicFilterDTO
{

	private String documento;
	private String producto;
	private String productoTercero;
	private String productoCodigo;
	private String productoImagen;
	private String productoDocumento;
	private String nombre;
	private Integer cantidadPromocion;
	private Integer cantidadPromocionBase;
	private String plantilla;
	private String transaccionRegistro;
	private String transaccionInactivo;

	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	
	
	public void setProducto(String producto) {
		this.producto = producto;
	}
	
	public String getProducto() {
		return producto;
	}
	
	
	public void setProductoTercero(String productoTercero) {
		this.productoTercero = productoTercero;
	}
	
	public String getProductoTercero() {
		return productoTercero;
	}
	
	
	public void setProductoCodigo(String productoCodigo) {
		this.productoCodigo = productoCodigo;
	}
	
	public String getProductoCodigo() {
		return productoCodigo;
	}
	
	
	public void setProductoImagen(String productoImagen) {
		this.productoImagen = productoImagen;
	}
	
	public String getProductoImagen() {
		return productoImagen;
	}
	
	
	public void setProductoDocumento(String productoDocumento) {
		this.productoDocumento = productoDocumento;
	}
	
	public String getProductoDocumento() {
		return productoDocumento;
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
	
					
	
	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}
	
	public String getPlantilla() {
		return plantilla;
	}
	
					
	
	public void setTransaccionRegistro(String transaccionRegistro) {
		this.transaccionRegistro = transaccionRegistro;
	}
	
	public String getTransaccionRegistro() {
		return transaccionRegistro;
	}
	
	
	public void setTransaccionInactivo(String transaccionInactivo) {
		this.transaccionInactivo = transaccionInactivo;
	}
	
	public String getTransaccionInactivo() {
		return transaccionInactivo;
	}
	

}