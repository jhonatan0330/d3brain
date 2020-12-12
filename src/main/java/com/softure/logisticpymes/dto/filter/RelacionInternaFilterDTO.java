package com.softure.logisticpymes.dto.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("RelacionInternaFilterDTO")
public class RelacionInternaFilterDTO extends BasicFilterDTO
{

	private String propiedad;
	private String propiedadNombre;
	private String plantilla;
	private String plantillaNombre;
	private String campo;
	private String campoNombre;

	
	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}
	
	public String getPropiedad() {
		return propiedad;
	}
	
	
	public void setPropiedadNombre(String propiedadNombre) {
		this.propiedadNombre = propiedadNombre;
	}
	
	public String getPropiedadNombre() {
		return propiedadNombre;
	}
	
	
	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}
	
	public String getPlantilla() {
		return plantilla;
	}
	
	
	public void setPlantillaNombre(String plantillaNombre) {
		this.plantillaNombre = plantillaNombre;
	}
	
	public String getPlantillaNombre() {
		return plantillaNombre;
	}
	
	
	public void setCampo(String campo) {
		this.campo = campo;
	}
	
	public String getCampo() {
		return campo;
	}
	
	
	public void setCampoNombre(String campoNombre) {
		this.campoNombre = campoNombre;
	}
	
	public String getCampoNombre() {
		return campoNombre;
	}
	

}