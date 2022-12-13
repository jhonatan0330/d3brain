package com.softure.process_form.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;
@Alias("PlantillaConsecutivoFilterDTO")
public class PlantillaConsecutivoFilterDTO extends BasicFilterDTO
{

	private String caracteristica;
	private String caracteristicaNombre;
	private String valorOpcion;
	private String opcionNombre;
	private String consecutivo;
	private String consecutivoNombre;

	
	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}
	
	public String getCaracteristica() {
		return caracteristica;
	}
	
	
	public void setCaracteristicaNombre(String caracteristicaNombre) {
		this.caracteristicaNombre = caracteristicaNombre;
	}
	
	public String getCaracteristicaNombre() {
		return caracteristicaNombre;
	}
	
	
	public void setValorOpcion(String valorOpcion) {
		this.valorOpcion = valorOpcion;
	}
	
	public String getValorOpcion() {
		return valorOpcion;
	}
	
	
	public void setOpcionNombre(String opcionNombre) {
		this.opcionNombre = opcionNombre;
	}
	
	public String getOpcionNombre() {
		return opcionNombre;
	}
	
	
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	
	public String getConsecutivo() {
		return consecutivo;
	}
	
	
	public void setConsecutivoNombre(String consecutivoNombre) {
		this.consecutivoNombre = consecutivoNombre;
	}
	
	public String getConsecutivoNombre() {
		return consecutivoNombre;
	}
	

}