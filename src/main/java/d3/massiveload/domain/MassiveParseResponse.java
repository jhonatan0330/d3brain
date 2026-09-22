package d3.massiveload.domain;

import java.util.ArrayList;
import java.util.List;

public class MassiveParseResponse {

	private List<MassiveParseLineDTO> lines = new ArrayList<>();
	private List<String> camposSinValidar = new ArrayList<>();

	public List<MassiveParseLineDTO> getLines() {
		return lines;
	}

	public void setLines(List<MassiveParseLineDTO> lines) {
		this.lines = lines;
	}

	public List<String> getCamposSinValidar() {
		return camposSinValidar;
	}

	public void setCamposSinValidar(List<String> camposSinValidar) {
		this.camposSinValidar = camposSinValidar;
	}
}