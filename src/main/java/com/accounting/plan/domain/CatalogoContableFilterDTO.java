package com.accounting.plan.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;
@Alias("CatalogoContableFilterDTO")
public class CatalogoContableFilterDTO extends BasicFilterDTO
{

	private String nombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicialMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinalMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinalMax;
	private String codigo;

	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setFechaInicialMin(Date fechaInicialMin) {
		this.fechaInicialMin = fechaInicialMin;
	}
	
	public Date getFechaInicialMin() {
		return fechaInicialMin;
	}
	
	public void setFechaInicialMax(Date fechaInicialMax) {
		this.fechaInicialMax = fechaInicialMax;
	}
	
	public Date getFechaInicialMax() {
		return fechaInicialMax;
	}
	
	
	public void setFechaFinalMin(Date fechaFinalMin) {
		this.fechaFinalMin = fechaFinalMin;
	}
	
	public Date getFechaFinalMin() {
		return fechaFinalMin;
	}
	
	public void setFechaFinalMax(Date fechaFinalMax) {
		this.fechaFinalMax = fechaFinalMax;
	}
	
	public Date getFechaFinalMax() {
		return fechaFinalMax;
	}
	
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	

}