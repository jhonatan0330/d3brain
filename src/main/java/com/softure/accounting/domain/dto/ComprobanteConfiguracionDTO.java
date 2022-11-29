package com.softure.accounting.domain.dto;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicDTO;

@Alias("ComprobanteConfiguracionDTO")
public class ComprobanteConfiguracionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String plantilla;
	private String catalogo;

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}
	
	public String getPlantilla() {
		return plantilla;
	}
	public void setCatalogo(String catalogo) {
		this.catalogo = catalogo;
	}
	
	public String getCatalogo() {
		return catalogo;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}