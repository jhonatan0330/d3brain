package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("CategoriaProductoFilterDTO")
public class CategoriaProductoFilterDTO extends BasicFilterDTO
{

	private String nombre;
	private String imagen;
	private String nodoSuperior;
 	private Boolean inventariosFilter = null;
 	private Boolean camposAdicionalesFilter = null;
 	private Boolean composicionFilter = null;
	private Integer promocionBase;

	
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
	
	
	public void setNodoSuperior(String nodoSuperior) {
		this.nodoSuperior = nodoSuperior;
	}
	
	public String getNodoSuperior() {
		return nodoSuperior;
	}
	
					
 	
 	public void setInventariosFilter(Boolean inventariosFilter) {
		this.inventariosFilter = inventariosFilter;
	}
	
	public Boolean getInventariosFilter() {
		return inventariosFilter;
	}
	
 	
 	public void setCamposAdicionalesFilter(Boolean camposAdicionalesFilter) {
		this.camposAdicionalesFilter = camposAdicionalesFilter;
	}
	
	public Boolean getCamposAdicionalesFilter() {
		return camposAdicionalesFilter;
	}
	
 	
 	public void setComposicionFilter(Boolean composicionFilter) {
		this.composicionFilter = composicionFilter;
	}
	
	public Boolean getComposicionFilter() {
		return composicionFilter;
	}
	
	
	public void setPromocionBase(Integer promocionBase) {
		this.promocionBase = promocionBase;
	}
	
	public Integer getPromocionBase() {
		return promocionBase;
	}
	

}