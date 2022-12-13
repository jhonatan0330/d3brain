package com.softure.accounting.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("ComprobanteConfiguracionDetalleDTO")
public class ComprobanteConfiguracionDetalleDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String configuracion;
	private String cuenta;
	private String valorDebe;
	private String valorHaber;

	public void setConfiguracion(String configuracion) {
		this.configuracion = configuracion;
	}
	
	public String getConfiguracion() {
		return configuracion;
	}
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	public String getCuenta() {
		return cuenta;
	}
	public void setValorDebe(String valorDebe) {
		this.valorDebe = valorDebe;
	}
	
	public String getValorDebe() {
		return valorDebe;
	}
	public void setValorHaber(String valorHaber) {
		this.valorHaber = valorHaber;
	}
	
	public String getValorHaber() {
		return valorHaber;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}