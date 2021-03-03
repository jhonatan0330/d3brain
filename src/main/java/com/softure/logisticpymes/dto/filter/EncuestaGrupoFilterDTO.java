package com.softure.logisticpymes.dto.filter;

// BEGIN region interImport
import java.util.List;
import com.softure.logisticpymes.dto.EncuestaRespuestaDTO;
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("EncuestaGrupoFilterDTO")
public class EncuestaGrupoFilterDTO extends BasicFilterDTO
{

	private String codigo;
	private String nombre;
	private String encuesta;
	private Integer numeroPreguntas;
	private Integer numeroRespuestasUsuario;
	private List<EncuestaRespuestaDTO> respuestas;
	private String usuario;

	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setEncuesta(String encuesta) {
		this.encuesta = encuesta;
	}
	
	public String getEncuesta() {
		return encuesta;
	}
	
	
	public void setNumeroPreguntas(Integer numeroPreguntas) {
		this.numeroPreguntas = numeroPreguntas;
	}
	
	public Integer getNumeroPreguntas() {
		return numeroPreguntas;
	}
	
	
	public void setNumeroRespuestasUsuario(Integer numeroRespuestasUsuario) {
		this.numeroRespuestasUsuario = numeroRespuestasUsuario;
	}
	
	public Integer getNumeroRespuestasUsuario() {
		return numeroRespuestasUsuario;
	}
	
	
	public void setRespuestas(List<EncuestaRespuestaDTO> respuestas) {
		this.respuestas = respuestas;
	}
	
	public List<EncuestaRespuestaDTO> getRespuestas() {
		return respuestas;
	}
	
					
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
					

}