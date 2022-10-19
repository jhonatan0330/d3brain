package com.softure.logisticpymes.domain.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("PostRespuestaDTO")
public class PostRespuestaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private Integer calificacionesPositivas;
	private Integer calificacionesNegativas;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String autor;
	private String autorNombre;
	private String autorImagen;
	private String pregunta;
	private String respuesta;

	public void setCalificacionesPositivas(Integer calificacionesPositivas) {
		this.calificacionesPositivas = calificacionesPositivas;
	}
	
	public Integer getCalificacionesPositivas() {
		return calificacionesPositivas;
	}
	public void setCalificacionesNegativas(Integer calificacionesNegativas) {
		this.calificacionesNegativas = calificacionesNegativas;
	}
	
	public Integer getCalificacionesNegativas() {
		return calificacionesNegativas;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	
	public String getAutor() {
		return autor;
	}
	public void setAutorNombre(String autorNombre) {
		this.autorNombre = autorNombre;
	}
	
	public String getAutorNombre() {
		return autorNombre;
	}
	public void setAutorImagen(String autorImagen) {
		this.autorImagen = autorImagen;
	}
	
	public String getAutorImagen() {
		return autorImagen;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	
	public String getPregunta() {
		return pregunta;
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	public String getRespuesta() {
		return respuesta;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}