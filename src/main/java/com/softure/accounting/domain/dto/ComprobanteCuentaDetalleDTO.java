package com.softure.accounting.domain.dto;

import java.math.BigDecimal;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("ComprobanteCuentaDetalleDTO")
public class ComprobanteCuentaDetalleDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String cuenta;
	private BigDecimal debe;
	private BigDecimal haber;
	private String comprobante;

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	public String getCuenta() {
		return cuenta;
	}
	public void setDebe(BigDecimal debe) {
		this.debe = debe;
	}
	
	public BigDecimal getDebe() {
		return debe;
	}
	public void setHaber(BigDecimal haber) {
		this.haber = haber;
	}
	
	public BigDecimal getHaber() {
		return haber;
	}
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}
	
	public String getComprobante() {
		return comprobante;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}