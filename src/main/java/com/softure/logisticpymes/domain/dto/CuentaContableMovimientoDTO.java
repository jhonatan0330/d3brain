package com.softure.logisticpymes.domain.dto;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("CuentaContableMovimientoDTO")
public class CuentaContableMovimientoDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private BigDecimal montoAplicado;
	private BigDecimal saldoInicial;
	private BigDecimal saldoFinal;
	private String anterior;
	private String siguiente;
	private String comprobante;
	private String cuenta;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEvento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistro;

	public void setMontoAplicado(BigDecimal montoAplicado) {
		this.montoAplicado = montoAplicado;
	}
	
	public BigDecimal getMontoAplicado() {
		return montoAplicado;
	}
	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}
	
	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}
	public void setSaldoFinal(BigDecimal saldoFinal) {
		this.saldoFinal = saldoFinal;
	}
	
	public BigDecimal getSaldoFinal() {
		return saldoFinal;
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
	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}
	
	public Date getFechaEvento() {
		return fechaEvento;
	}
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	
	public Date getFechaRegistro() {
		return fechaRegistro;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}