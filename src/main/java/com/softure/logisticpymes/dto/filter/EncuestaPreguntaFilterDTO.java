package com.softure.logisticpymes.dto.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("EncuestaPreguntaFilterDTO")
public class EncuestaPreguntaFilterDTO extends BasicFilterDTO
{

	private String codigo;
	private String grupo;
	private String grupoNombre;
	private String grupoCodigo;
	private String tipo;
	private String restriccion;

	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
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
	
	
	public void setRestriccion(String restriccion) {
		this.restriccion = restriccion;
	}
	
	public String getRestriccion() {
		return restriccion;
	}
	
					

}