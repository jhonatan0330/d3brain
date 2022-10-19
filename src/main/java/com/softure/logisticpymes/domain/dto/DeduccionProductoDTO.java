package com.softure.logisticpymes.domain.dto;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("DeduccionProductoDTO")
public class DeduccionProductoDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String documento;
	private String producto;
	private String productoNombre;
	private String productoCodigo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private BigDecimal cantidad;
	private String responsable;
	private String responsableNombre;
	private String bodega;

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
	public void setProductoNombre(String productoNombre) {
		this.productoNombre = productoNombre;
	}
	
	public String getProductoNombre() {
		return productoNombre;
	}
	public void setProductoCodigo(String productoCodigo) {
		this.productoCodigo = productoCodigo;
	}
	
	public String getProductoCodigo() {
		return productoCodigo;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}
	
	public BigDecimal getCantidad() {
		return cantidad;
	}
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
	public String getResponsable() {
		return responsable;
	}
	public void setResponsableNombre(String responsableNombre) {
		this.responsableNombre = responsableNombre;
	}
	
	public String getResponsableNombre() {
		return responsableNombre;
	}
	public void setBodega(String bodega) {
		this.bodega = bodega;
	}
	
	public String getBodega() {
		return bodega;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}