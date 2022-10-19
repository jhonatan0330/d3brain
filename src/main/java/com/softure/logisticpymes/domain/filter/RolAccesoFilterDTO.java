package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("RolAccesoFilterDTO")
public class RolAccesoFilterDTO extends BasicFilterDTO
{

	private String plantilla;
	private String nombre;
	private String codigo;
	private String imagen;
 	private Boolean permisosCompletosFilter = null;
	private Integer minutosSesion;

	
	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}
	
	public String getPlantilla() {
		return plantilla;
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
	
	
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	
 	
 	public void setPermisosCompletosFilter(Boolean permisosCompletosFilter) {
		this.permisosCompletosFilter = permisosCompletosFilter;
	}
	
	public Boolean getPermisosCompletosFilter() {
		return permisosCompletosFilter;
	}
	
	
	public void setMinutosSesion(Integer minutosSesion) {
		this.minutosSesion = minutosSesion;
	}
	
	public Integer getMinutosSesion() {
		return minutosSesion;
	}
	

}