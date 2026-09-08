package d3.configuration.domain;

import d3.shared.domain.BasicFilterDTO;

public class ArbolConfiguracionFilterDTO extends BasicFilterDTO {

	public static final String PROFUNDIDAD_COMPLETA = "COMPLETA";
	public static final String PROFUNDIDAD_SIMPLE = "SIMPLE";

	private String profundidad;
	private Boolean listarPropiedades;

	public void setProfundidad(String profundidad) {
		this.profundidad = profundidad;
	}

	public String getProfundidad() {
		return profundidad;
	}

	public void setListarPropiedades(Boolean listarPropiedades) {
		this.listarPropiedades = listarPropiedades;
	}

	public Boolean getListarPropiedades() {
		return listarPropiedades;
	}

}