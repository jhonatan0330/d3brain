package com.softure.logisticpymes.domain.filter;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("CuentaAuxiliarPlantillaFilterDTO")
public class CuentaAuxiliarPlantillaFilterDTO extends BasicFilterDTO
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
	

}