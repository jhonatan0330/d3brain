package com.softure.accounting.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;
@Alias("CuentaContableMovimientoFilterDTO")
public class CuentaContableMovimientoFilterDTO extends BasicFilterDTO
{

	private String anterior;
	private String siguiente;
	private String comprobante;
	private String cuenta;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEventoMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEventoMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMax;

	
	public void setAnterior(String anterior) {
		this.anterior = anterior;
	}
	
	public String getAnterior() {
		return anterior;
	}
	
	
	public void setSiguiente(String siguiente) {
		this.siguiente = siguiente;
	}
	
	public String getSiguiente() {
		return siguiente;
	}
	
	
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}
	
	public String getComprobante() {
		return comprobante;
	}
	
	
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	public String getCuenta() {
		return cuenta;
	}
	
	
	public void setFechaEventoMin(Date fechaEventoMin) {
		this.fechaEventoMin = fechaEventoMin;
	}
	
	public Date getFechaEventoMin() {
		return fechaEventoMin;
	}
	
	public void setFechaEventoMax(Date fechaEventoMax) {
		this.fechaEventoMax = fechaEventoMax;
	}
	
	public Date getFechaEventoMax() {
		return fechaEventoMax;
	}
	
	
	public void setFechaRegistroMin(Date fechaRegistroMin) {
		this.fechaRegistroMin = fechaRegistroMin;
	}
	
	public Date getFechaRegistroMin() {
		return fechaRegistroMin;
	}
	
	public void setFechaRegistroMax(Date fechaRegistroMax) {
		this.fechaRegistroMax = fechaRegistroMax;
	}
	
	public Date getFechaRegistroMax() {
		return fechaRegistroMax;
	}
	

}