package com.softure.logisticpymes.domain.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("CambioDTO")
public class CambioDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String nombre;
	private String motivo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaAplicacion;
	private String sesionActiva;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	
	public String getMotivo() {
		return motivo;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setFechaAplicacion(Date fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}
	
	public Date getFechaAplicacion() {
		return fechaAplicacion;
	}
	public void setSesionActiva(String sesionActiva) {
		this.sesionActiva = sesionActiva;
	}
	
	public String getSesionActiva() {
		return sesionActiva;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}