package com.softure.inventory.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("DeduccionProductoFilterDTO")
public class DeduccionProductoFilterDTO extends BasicFilterDTO {

	private String documento;
	private String producto;
	private String productoNombre;
	private String productoCodigo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
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

}