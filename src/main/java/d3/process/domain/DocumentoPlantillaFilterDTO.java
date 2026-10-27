package d3.process.domain;


import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicNodoFilter;

@Alias("DocumentoPlantillaFilterDTO")
public class DocumentoPlantillaFilterDTO extends BasicNodoFilter {

	private String proceso;

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProceso() {
		return proceso;
	}
}
