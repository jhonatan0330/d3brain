package com.accounting.plan.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("CatalogoContableDTO")
public class CatalogoContableDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String nombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicial;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinal;
	private String codigo;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	
	public Date getFechaInicial() {
		return fechaInicial;
	}
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	public Date getFechaFinal() {
		return fechaFinal;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}