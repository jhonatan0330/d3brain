package d3.shared.domain;

import java.util.List;

import d3.property.domain.PropiedadDTO;

public abstract class BasicParamDTO extends BasicDTO {

	private List<PropiedadDTO> propiedades;

	public List<PropiedadDTO> getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(List<PropiedadDTO> propiedades) {
		this.propiedades = propiedades;
	}

}
