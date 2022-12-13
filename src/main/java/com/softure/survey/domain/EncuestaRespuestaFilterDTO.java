package com.softure.survey.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;
@Alias("EncuestaRespuestaFilterDTO")
public class EncuestaRespuestaFilterDTO extends BasicFilterDTO
{

	private String pregunta;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String usuario;
 	private Boolean respuestaBooleanFilter = null;
	private String respuestaOpcion;

	
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	
	public String getPregunta() {
		return pregunta;
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
	
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
 	
 	public void setRespuestaBooleanFilter(Boolean respuestaBooleanFilter) {
		this.respuestaBooleanFilter = respuestaBooleanFilter;
	}
	
	public Boolean getRespuestaBooleanFilter() {
		return respuestaBooleanFilter;
	}
	
	
	public void setRespuestaOpcion(String respuestaOpcion) {
		this.respuestaOpcion = respuestaOpcion;
	}
	
	public String getRespuestaOpcion() {
		return respuestaOpcion;
	}
	

}