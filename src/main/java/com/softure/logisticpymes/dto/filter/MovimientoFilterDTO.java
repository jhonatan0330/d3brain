package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("MovimientoFilterDTO")
public class MovimientoFilterDTO extends BasicFilterDTO
{

	private String tipo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEventoMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEventoMax;
	private String cuenta;
	private String cuentaNombre;
	private String turno;
	private String anterior;
	private String siguiente;
	private String relacionado;
	private String documento;

	
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
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
	
	
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	public String getCuenta() {
		return cuenta;
	}
	
	
	public void setCuentaNombre(String cuentaNombre) {
		this.cuentaNombre = cuentaNombre;
	}
	
	public String getCuentaNombre() {
		return cuentaNombre;
	}
	
	
	public void setTurno(String turno) {
		this.turno = turno;
	}
	
	public String getTurno() {
		return turno;
	}
	
	
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
	
	
	public void setRelacionado(String relacionado) {
		this.relacionado = relacionado;
	}
	
	public String getRelacionado() {
		return relacionado;
	}
	
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	

}