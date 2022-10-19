package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("CuentaContableFilterDTO")
public class CuentaContableFilterDTO extends BasicFilterDTO
{

	private String codigo;
	private String nombre;
	private String catalogo;
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
	
	
	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
	}
	
	public String getCatalogo() {
		return catalogo;
	}
	
	
	public void setCuentaPadre(String cuentaPadre) {
		this.cuentaPadre = cuentaPadre;
	}
	
	public String getCuentaPadre() {
		return cuentaPadre;
	}
	

}