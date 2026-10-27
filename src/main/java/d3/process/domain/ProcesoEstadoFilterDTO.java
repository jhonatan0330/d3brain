package d3.process.domain;


import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicNodoFilter;

@Alias("ProcesoEstadoFilterDTO")
public class ProcesoEstadoFilterDTO extends BasicNodoFilter {

	private String estadoDocumento;

	public String getEstadoDocumento() {
		return estadoDocumento;
	}

	public void setEstadoDocumento(String estadoDocumento) {
		this.estadoDocumento = estadoDocumento;
	}

}
