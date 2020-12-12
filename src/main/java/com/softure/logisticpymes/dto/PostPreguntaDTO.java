package com.softure.logisticpymes.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("PostPreguntaDTO")
public class PostPreguntaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String autor;
	private String autorImagen;
	private String autorNombre;
	private String pregunta;
	private String keywords;
	private Integer calificaciones;

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
	public void setAutorImagen(String autorImagen) {
		this.autorImagen = autorImagen;
	}
	
	public String getAutorImagen() {
		return autorImagen;
	}
	public void setAutorNombre(String autorNombre) {
		this.autorNombre = autorNombre;
	}
	
	public String getAutorNombre() {
		return autorNombre;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	
	public String getPregunta() {
		return pregunta;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public String getKeywords() {
		return keywords;
	}
	public void setCalificaciones(Integer calificaciones) {
		this.calificaciones = calificaciones;
	}
	
	public Integer getCalificaciones() {
		return calificaciones;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}