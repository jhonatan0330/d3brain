package com.softure.logisticpymes.domain;

import java.util.List;

import com.softure.java.domain.BasicDTO;
import com.softure.property.domain.PropiedadDTO;

public abstract class BasicParamDTO extends BasicDTO {

	private List<PropiedadDTO> propiedades;

	public List<PropiedadDTO> getPropiedades() {
		return propiedades;
	}


	public void setPropiedades(List<PropiedadDTO> propiedades) {
		this.propiedades = propiedades;
	}	

}
