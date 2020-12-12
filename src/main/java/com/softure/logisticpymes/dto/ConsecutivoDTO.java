package com.softure.logisticpymes.dto;

import java.math.BigDecimal;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("ConsecutivoDTO")
public class ConsecutivoDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String nombre;
	private String prefijo;
	private String sufijo;
	private BigDecimal numeroInicial;
	private BigDecimal numeroFinal;
	private BigDecimal numeroActual;
	private boolean manual;
	private String consecutivoActual;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
	
	public String getPrefijo() {
		return prefijo;
	}
	public void setSufijo(String sufijo) {
		this.sufijo = sufijo;
	}
	
	public String getSufijo() {
		return sufijo;
	}
	public void setNumeroInicial(BigDecimal numeroInicial) {
		this.numeroInicial = numeroInicial;
	}
	
	public BigDecimal getNumeroInicial() {
		return numeroInicial;
	}
	public void setNumeroFinal(BigDecimal numeroFinal) {
		this.numeroFinal = numeroFinal;
	}
	
	public BigDecimal getNumeroFinal() {
		return numeroFinal;
	}
	public void setNumeroActual(BigDecimal numeroActual) {
		this.numeroActual = numeroActual;
	}
	
	public BigDecimal getNumeroActual() {
		return numeroActual;
	}
	public void setManual(boolean manual) {
		this.manual = manual;
	}
	
	public boolean getManual() {
		return manual;
	}
	public void setConsecutivoActual(String consecutivoActual) {
		this.consecutivoActual = consecutivoActual;
	}
	
	public String getConsecutivoActual() {
		return consecutivoActual;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}