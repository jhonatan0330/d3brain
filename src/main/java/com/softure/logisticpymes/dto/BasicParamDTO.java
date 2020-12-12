package com.softure.logisticpymes.dto;

import java.util.List;

public abstract class BasicParamDTO extends BasicDTO {

	private List<PropiedadDTO> propiedades;

	public List<PropiedadDTO> getPropiedades() {
		return propiedades;
	}


	public void setPropiedades(List<PropiedadDTO> propiedades) {
		this.propiedades = propiedades;
	}	

}
