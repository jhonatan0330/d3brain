package com.softure.logisticpymes.domain.dto;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("CuentaAuxiliarDocumentoDTO")
public class CuentaAuxiliarDocumentoDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String documento;
	private String cuentaAuxiliar;

	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	public void setCuentaAuxiliar(String cuentaAuxiliar) {
		this.cuentaAuxiliar = cuentaAuxiliar;
	}
	
	public String getCuentaAuxiliar() {
		return cuentaAuxiliar;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}