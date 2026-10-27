package d3.process.domain;


import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicNodoDTO;

@Alias("ProcesoTransicionDTO")
public class ProcesoTransicionDTO extends BasicNodoDTO
{
	public static final String RESTANDO = "R";
	public static final String SUMANDO = "S";

	private String plantilla;
	private String plantillaNombre;

	private Integer estadoPartidaOrden;
	private String estadoPartida;
	private String estadoPartidaNombre;
	
	private String estadoLLegada;
	private String estadoLlegadaNombre;
	private Integer estadoLlegadaOrden;
	private String estadoLlegadaTipo;


	public void setEstadoPartidaOrden(Integer estadoPartidaOrden) {
		this.estadoPartidaOrden = estadoPartidaOrden;
	}

	public Integer getEstadoPartidaOrden() {
		return estadoPartidaOrden;
	}

	public void setEstadoLlegadaOrden(Integer estadoLlegadaOrden) {
		this.estadoLlegadaOrden = estadoLlegadaOrden;
	}

	public Integer getEstadoLlegadaOrden() {
		return estadoLlegadaOrden;
	}


	public void setEstadoPartida(String estadoPartida) {
		this.estadoPartida = estadoPartida;
	}

	public String getEstadoPartida() {
		return estadoPartida;
	}

	public void setEstadoPartidaNombre(String estadoPartidaNombre) {
		this.estadoPartidaNombre = estadoPartidaNombre;
	}

	public String getEstadoPartidaNombre() {
		return estadoPartidaNombre;
	}

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantillaNombre(String plantillaNombre) {
		this.plantillaNombre = plantillaNombre;
	}

	public String getPlantillaNombre() {
		return plantillaNombre;
	}

	
	public void setEstadoLLegada(String estadoLLegada) {
		this.estadoLLegada = estadoLLegada;
	}

	public String getEstadoLLegada() {
		return estadoLLegada;
	}

	public void setEstadoLlegadaNombre(String estadoLlegadaNombre) {
		this.estadoLlegadaNombre = estadoLlegadaNombre;
	}

	public String getEstadoLlegadaNombre() {
		return estadoLlegadaNombre;
	}

	public void setEstadoLlegadaTipo(String estadoLlegadaTipo) {
		this.estadoLlegadaTipo = estadoLlegadaTipo;
	}

	public String getEstadoLlegadaTipo() {
		return estadoLlegadaTipo;
	}
	
}