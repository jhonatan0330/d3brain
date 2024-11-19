package com.softure.inventory.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("ProductoInventarioFilterDTO")
public class ProductoInventarioFilterDTO extends BasicFilterDTO {

	private String producto;
	private String nombre;
	private String codigo;
	private String bodega;
	private String nombreBodega;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMax;
	private String documento;

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

	public void setFechaInicialMin(Date fechaInicialMin) {
		this.fechaInicialMin = fechaInicialMin;
	}

	public Date getFechaInicialMin() {
		return fechaInicialMin;
	}

	public void setFechaInicialMax(Date fechaInicialMax) {
		this.fechaInicialMax = fechaInicialMax;
	}

	public Date getFechaInicialMax() {
		return fechaInicialMax;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

}