package d3.configuration.domain;

import d3.shared.domain.BasicParamDTO;

public class ArbolNodoRequestDTO extends BasicParamDTO {

	private String camino;
	private String tipo;

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

}
