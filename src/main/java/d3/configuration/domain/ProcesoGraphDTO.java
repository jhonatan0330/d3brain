package d3.configuration.domain;

import java.util.List;

import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.shared.domain.BasicNodoDTO;

public class ProcesoGraphDTO extends BasicNodoDTO
{
	public static final String AGRUPADOR = "A";
	public static final String EJECUTOR = "E";

	private List<ProcesoDTO> hijos;
	private List<ProcesoEstadoDTO> estados;
	private List<ProcesoTransicionDTO> transiciones;
	private List<DocumentoPlantillaDTO> plantillas;
	
	public void setHijos(List<ProcesoDTO> hijos) {
		this.hijos = hijos;
	}

	public List<ProcesoDTO> getHijos() {
		return hijos;
	}

	public void setEstados(List<ProcesoEstadoDTO> estados) {
		this.estados = estados;
	}

	public List<ProcesoEstadoDTO> getEstados() {
		return estados;
	}

	public void setTransiciones(List<ProcesoTransicionDTO> transiciones) {
		this.transiciones = transiciones;
	}

	public List<ProcesoTransicionDTO> getTransiciones() {
		return transiciones;
	}

	public void setPlantillas(List<DocumentoPlantillaDTO> plantillas) {
		this.plantillas = plantillas;
	}

	public List<DocumentoPlantillaDTO> getPlantillas() {
		return plantillas;
	}

}