package d3.configuration.domain;

import d3.shared.domain.BasicParamDTO;

public class ArbolNodoRequestDTO extends BasicParamDTO {

	private String camino;
	private String tipo;
	private Boolean listarPropiedades;

	public String getCamino() {
		return camino;
	}

	public void setCamino(String camino) {
		this.camino = camino;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Boolean getListarPropiedades() {
		return listarPropiedades;
	}

	public void setListarPropiedades(Boolean listarPropiedades) {
		this.listarPropiedades = listarPropiedades;
	}

}
