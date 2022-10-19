package com.softure.logisticpymes.domain.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("PostCalificacionDTO")
public class PostCalificacionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String usuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String respuesta;
	private boolean positiva;

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	public String getRespuesta() {
		return respuesta;
	}
	public void setPositiva(boolean positiva) {
		this.positiva = positiva;
	}
	
	public boolean getPositiva() {
		return positiva;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}