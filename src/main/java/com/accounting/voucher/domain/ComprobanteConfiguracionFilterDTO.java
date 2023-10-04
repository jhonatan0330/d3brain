package com.accounting.voucher.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("ComprobanteConfiguracionFilterDTO")
public class ComprobanteConfiguracionFilterDTO extends BasicFilterDTO
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
	

}