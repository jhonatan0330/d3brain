package com.softure.logisticpymes.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("EncuestaRespuestaDTO")
public class EncuestaRespuestaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String pregunta;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String usuario;
	private boolean respuestaBoolean;
	private String respuestaOpcion;
	private String comentario;

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	
	public String getPregunta() {
		return pregunta;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setRespuestaBoolean(boolean respuestaBoolean) {
		this.respuestaBoolean = respuestaBoolean;
	}
	
	public boolean getRespuestaBoolean() {
		return respuestaBoolean;
	}
	public void setRespuestaOpcion(String respuestaOpcion) {
		this.respuestaOpcion = respuestaOpcion;
	}
	
	public String getRespuestaOpcion() {
		return respuestaOpcion;
	}
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
	public String getComentario() {
		return comentario;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}