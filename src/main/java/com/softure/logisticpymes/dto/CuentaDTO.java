package com.softure.logisticpymes.dto;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("CuentaDTO")
public class CuentaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String codigo;
	private String nombre;
	private BigDecimal saldo;
	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaConciliacion;
	private boolean validarTurno;

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	public void setFechaConciliacion(Date fechaConciliacion) {
		this.fechaConciliacion = fechaConciliacion;
	}
	
	public Date getFechaConciliacion() {
		return fechaConciliacion;
	}
	public void setValidarTurno(boolean validarTurno) {
		this.validarTurno = validarTurno;
	}
	
	public boolean getValidarTurno() {
		return validarTurno;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}