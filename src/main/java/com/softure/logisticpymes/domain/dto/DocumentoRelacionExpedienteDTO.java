package com.softure.logisticpymes.domain.dto;

import java.math.BigDecimal;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("DocumentoRelacionExpedienteDTO")
public class DocumentoRelacionExpedienteDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String campoMaestro;
	private String expedienteDetalle;
	private String transaccionRegistro;
	private String transaccionInactivo;
	private BigDecimal valor;

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
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public BigDecimal getValor() {
		return valor;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}