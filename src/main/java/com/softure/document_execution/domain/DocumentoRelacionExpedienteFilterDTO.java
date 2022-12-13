package com.softure.document_execution.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("DocumentoRelacionExpedienteFilterDTO")
public class DocumentoRelacionExpedienteFilterDTO extends BasicFilterDTO
{

	private String campoMaestro;
	private String expedienteDetalle;
	private String transaccionRegistro;
	private String transaccionInactivo;

	
	public void setCampoMaestro(String campoMaestro) {
		this.campoMaestro = campoMaestro;
	}
	
	public String getCampoMaestro() {
		return campoMaestro;
	}
	
	
	public void setExpedienteDetalle(String expedienteDetalle) {
		this.expedienteDetalle = expedienteDetalle;
	}
	
	public String getExpedienteDetalle() {
		return expedienteDetalle;
	}
	
	
	public void setTransaccionRegistro(String transaccionRegistro) {
		this.transaccionRegistro = transaccionRegistro;
	}
	
	public String getTransaccionRegistro() {
		return transaccionRegistro;
	}
	
	
	public void setTransaccionInactivo(String transaccionInactivo) {
		this.transaccionInactivo = transaccionInactivo;
	}
	
	public String getTransaccionInactivo() {
		return transaccionInactivo;
	}
	

}