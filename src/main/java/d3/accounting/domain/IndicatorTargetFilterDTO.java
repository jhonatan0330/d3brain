package d3.accounting.domain;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObjectFilter;

@Alias("IndicatorTargetFilterDTO")
public class IndicatorTargetFilterDTO extends SharedDataObjectFilter {

	private String indicador;

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

}
