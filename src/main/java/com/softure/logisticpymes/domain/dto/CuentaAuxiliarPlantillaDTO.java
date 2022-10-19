package com.softure.logisticpymes.domain.dto;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("CuentaAuxiliarPlantillaDTO")
public class CuentaAuxiliarPlantillaDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String cuentaPrincipal;
	private String plantilla;

	public void setCuentaPrincipal(String cuentaPrincipal) {
		this.cuentaPrincipal = cuentaPrincipal;
	}
	
	public String getCuentaPrincipal() {
		return cuentaPrincipal;
	}
	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}
	
	public String getPlantilla() {
		return plantilla;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}