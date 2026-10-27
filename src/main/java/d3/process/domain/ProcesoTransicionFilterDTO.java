package d3.process.domain;


import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicNodoFilter;

@Alias("ProcesoTransicionFilterDTO")
public class ProcesoTransicionFilterDTO extends BasicNodoFilter {
	
	private String estadoPartida;
	private String plantilla;
	private String estadoLLegada;

	public void setEstadoPartida(String estadoPartida) {
		this.estadoPartida = estadoPartida;
	}

	public String getEstadoPartida() {
		return estadoPartida;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getPlantilla() {
		return plantilla;
	}	

	public void setEstadoLLegada(String estadoLLegada) {
		this.estadoLLegada = estadoLLegada;
	}

	public String getEstadoLLegada() {
		return estadoLLegada;
	}

}
