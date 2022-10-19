package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("ProductoCaracteristicaFilterDTO")
public class ProductoCaracteristicaFilterDTO extends BasicFilterDTO
{

	private String base;
	private String baseNombre;
	private String formato;
	private String nombre;
	private String codigo;
	private Integer orden;
	private String imagen;

	
	public void setBase(String base) {
		this.base = base;
	}
	
	public String getBase() {
		return base;
	}
	
	
	public void setBaseNombre(String baseNombre) {
		this.baseNombre = baseNombre;
	}
	
	public String getBaseNombre() {
		return baseNombre;
	}
	
	
	public void setFormato(String formato) {
		this.formato = formato;
	}
	
	public String getFormato() {
		return formato;
	}
	
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	
	
	public void setOrden(Integer orden) {
		this.orden = orden;
	}
	
	public Integer getOrden() {
		return orden;
	}
	
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	
					

}