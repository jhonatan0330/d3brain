package com.softure.logisticpymes.domain.dto;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("PermisoDTO")
public class PermisoDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String rolAcceso;
	private String rolAccesoNombre;
	private String modulo;
	private String moduloNombre;

	public void setRolAcceso(String rolAcceso) {
		this.rolAcceso = rolAcceso;
	}
	
	public String getRolAcceso() {
		return rolAcceso;
	}
	public void setRolAccesoNombre(String rolAccesoNombre) {
		this.rolAccesoNombre = rolAccesoNombre;
	}
	
	public String getRolAccesoNombre() {
		return rolAccesoNombre;
	}
	public void setModulo(String modulo) {
		this.modulo = modulo;
	}
	
	public String getModulo() {
		return modulo;
	}
	public void setModuloNombre(String moduloNombre) {
		this.moduloNombre = moduloNombre;
	}
	
	public String getModuloNombre() {
		return moduloNombre;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}