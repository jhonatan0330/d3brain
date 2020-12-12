package com.softure.logisticpymes.dto.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("DocumentoPlantillaFilterDTO")
public class DocumentoPlantillaFilterDTO extends BasicFilterDTO
{

	private String codigo;
	private String nombre;
	private String consecutivo;
	private String imagen;
	private String color;

	
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
	
	
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	
	public String getConsecutivo() {
		return consecutivo;
	}
	
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	
					
					
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	
					
					

}