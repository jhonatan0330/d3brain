package com.softure.logisticpymes.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("AuditoriaDTO")
public class AuditoriaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String usuario;
	private String clase;
	private String llaveClase;
	private String operacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setClase(String clase) {
		this.clase = clase;
	}
	
	public String getClase() {
		return clase;
	}
	public void setLlaveClase(String llaveClase) {
		this.llaveClase = llaveClase;
	}
	
	public String getLlaveClase() {
		return llaveClase;
	}
	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}
	
	public String getOperacion() {
		return operacion;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}