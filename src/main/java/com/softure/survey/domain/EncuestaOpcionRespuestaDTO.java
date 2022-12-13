package com.softure.survey.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("EncuestaOpcionRespuestaDTO")
public class EncuestaOpcionRespuestaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String codigo;
	private String nombre;
	private String imagen;
	private String pregunta;

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
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}
	
	public String getPregunta() {
		return pregunta;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}