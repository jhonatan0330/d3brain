package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("TrazabilidadProductoInventarioFilterDTO")
public class TrazabilidadProductoInventarioFilterDTO extends BasicFilterDTO
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String bodega;
	private String producto;
	private String productoNombre;
	private String deduccionProducto;
	private String responsable;

	
	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}
	
	public Date getFechaMin() {
		return fechaMin;
	}
	
	public void setFechaMax(Date fechaMax) {
		this.fechaMax = fechaMax;
	}
	
	public Date getFechaMax() {
		return fechaMax;
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
	

}