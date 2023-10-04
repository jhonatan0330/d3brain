package com.accounting.plan.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("CuentaAuxiliarDocumentoFilterDTO")
public class CuentaAuxiliarDocumentoFilterDTO extends BasicFilterDTO
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
	

}