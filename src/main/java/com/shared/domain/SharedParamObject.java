package com.shared.domain;

import java.util.List;

public class SharedParamObject extends SharedDataObject {

	private List<SharedPropertyDTO> propiedades;

	public List<SharedPropertyDTO> getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(List<SharedPropertyDTO> propiedades) {
		this.propiedades = propiedades;
	}
}
