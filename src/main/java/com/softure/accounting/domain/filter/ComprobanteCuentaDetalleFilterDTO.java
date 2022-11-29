package com.softure.accounting.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("ComprobanteCuentaDetalleFilterDTO")
public class ComprobanteCuentaDetalleFilterDTO extends BasicFilterDTO
{

	private String cuenta;
	private String comprobante;

	
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}
	
	public String getCuenta() {
		return cuenta;
	}
	
	
	public void setComprobante(String comprobante) {
		this.comprobante = comprobante;
	}
	
	public String getComprobante() {
		return comprobante;
	}
	

}