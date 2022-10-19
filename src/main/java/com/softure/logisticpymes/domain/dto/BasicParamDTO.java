package com.softure.logisticpymes.domain.dto;

import java.util.List;

import com.softure.java.domain.BasicDTO;

public abstract class BasicParamDTO extends BasicDTO {

	private List<PropiedadDTO> propiedades;

	public List<PropiedadDTO> getPropiedades() {
		return propiedades;
	}


	public void setPropiedades(List<PropiedadDTO> propiedades) {
		this.propiedades = propiedades;
	}	

}
