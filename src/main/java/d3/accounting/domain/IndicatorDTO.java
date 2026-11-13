package d3.accounting.domain;

import java.util.List;

import org.apache.ibatis.type.Alias;

import d3.configuration.domain.NodoArbolDTO;

@Alias("IndicatorDTO")
public class IndicatorDTO extends NodoArbolDTO {

	private List<IndicatorTargetDTO> targets;

	public List<IndicatorTargetDTO> getTargets() {
		return targets;
	}

	public void setTargets(List<IndicatorTargetDTO> targets) {
		this.targets = targets;
	}

}
