package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("PostPreguntaFilterDTO")
public class PostPreguntaFilterDTO extends BasicFilterDTO
{

	private String campo;
	private String tipo;
	private Integer calificaciones;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String autor;
	private String autorImagen;
	private String autorNombre;

	
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
	
					

}