package com.softure.inventory.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("BodegaDTO")
public class BodegaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String nombre;
	private String codigo;
	private String documento;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}