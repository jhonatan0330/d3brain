package com.softure.logisticpymes.domain.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;
@Alias("PostRespuestaFilterDTO")
public class PostRespuestaFilterDTO extends BasicFilterDTO
{

	private Integer calificacionesPositivas;
	private Integer calificacionesNegativas;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String autor;
	private String autorNombre;
	private String autorImagen;
	private String pregunta;

	
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
	
	
	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}
	
	public Date getFechaMin() {
		return fechaMin;
	}
	
	public void setFechaMax(Date fechaMax) {
		this.fechaMax = fechaMax;
	}
	
	public Date getFechaMax() {
		return fechaMax;
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
	

}