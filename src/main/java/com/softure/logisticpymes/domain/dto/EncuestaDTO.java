package com.softure.logisticpymes.domain.dto;

import java.util.List;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("EncuestaDTO")
public class EncuestaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String nombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEjecucion;
	private boolean colaborativa;
	private String rol;
	private String cliente;
	private List<EncuestaGrupoDTO> grupos;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}
	
	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}
	public void setColaborativa(boolean colaborativa) {
		this.colaborativa = colaborativa;
	}
	
	public boolean getColaborativa() {
		return colaborativa;
	}
	public void setRol(String rol) {
		this.rol = rol;
	}
	
	public String getRol() {
		return rol;
	}
	public void setCliente(String cliente) {
		this.cliente = cliente;
	}
	
	public String getCliente() {
		return cliente;
	}
	public void setGrupos(List<EncuestaGrupoDTO> grupos) {
		this.grupos = grupos;
	}
	
	public List<EncuestaGrupoDTO> getGrupos() {
		return grupos;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}