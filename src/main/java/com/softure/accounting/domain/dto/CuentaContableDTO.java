package com.softure.accounting.domain.dto;

import java.math.BigDecimal;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("CuentaContableDTO")
public class CuentaContableDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String codigo;
	private String nombre;
	private BigDecimal sobregiro;
	private String catalogo;
	private BigDecimal saldo;
	private String cuentaPadre;

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
	public void setSobregiro(BigDecimal sobregiro) {
		this.sobregiro = sobregiro;
	}
	
	public BigDecimal getSobregiro() {
		return sobregiro;
	}
	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
	}
	
	public String getCatalogo() {
		return catalogo;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setCuentaPadre(String cuentaPadre) {
		this.cuentaPadre = cuentaPadre;
	}
	
	public String getCuentaPadre() {
		return cuentaPadre;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}