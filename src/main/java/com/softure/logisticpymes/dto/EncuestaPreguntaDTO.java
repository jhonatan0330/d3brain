package com.softure.logisticpymes.dto;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("EncuestaPreguntaDTO")
public class EncuestaPreguntaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{
	public static final String TIPO_OPCION_MULTIPLE = "O";
	public static final String TIPO_BOOLEAN = "B";

	private String codigo;
	private String nombre;
	private String grupo;
	private String grupoNombre;
	private String grupoCodigo;
	private String tipo;
	private String descripcion;
	private String restriccion;

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
	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}
	
	public String getGrupo() {
		return grupo;
	}
	public void setGrupoNombre(String grupoNombre) {
		this.grupoNombre = grupoNombre;
	}
	
	public String getGrupoNombre() {
		return grupoNombre;
	}
	public void setGrupoCodigo(String grupoCodigo) {
		this.grupoCodigo = grupoCodigo;
	}
	
	public String getGrupoCodigo() {
		return grupoCodigo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public String getTipo() {
		return tipo;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setRestriccion(String restriccion) {
		this.restriccion = restriccion;
	}
	
	public String getRestriccion() {
		return restriccion;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}