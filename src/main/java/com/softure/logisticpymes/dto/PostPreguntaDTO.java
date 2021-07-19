package com.softure.logisticpymes.dto;

import java.util.List;

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
	public static final String DASHBOARD = "D";
	public static final String PLANTILLA = "P";
	public static final String CAMPO = "C";
	public static final String TRANSICION = "T";

	private String campo;
	private String tipo;
	private Integer calificaciones;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String autor;
	private String autorImagen;
	private String autorNombre;
	private String pregunta;
	private List<PostRespuestaDTO> respuestas;

	public void setCampo(String campo) {
		this.campo = campo;
	}
	
	public String getCampo() {
		return campo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setCalificaciones(Integer calificaciones) {
		this.calificaciones = calificaciones;
	}
	
	public Integer getCalificaciones() {
		return calificaciones;
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
	public void setRespuestas(List<PostRespuestaDTO> respuestas) {
		this.respuestas = respuestas;
	}
	
	public List<PostRespuestaDTO> getRespuestas() {
		return respuestas;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}