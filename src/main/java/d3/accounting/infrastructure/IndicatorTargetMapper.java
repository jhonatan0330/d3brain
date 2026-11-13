package d3.accounting.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting.domain.IndicatorTargetDTO;
import d3.accounting.domain.IndicatorTargetFilterDTO;

@D3SqlConnMapper(value = "IndicatorTargetAccountingMapper")
public interface IndicatorTargetMapper {

	IndicatorTargetDTO insert(IndicatorTargetDTO dto);

	IndicatorTargetDTO update(IndicatorTargetDTO dto);

	IndicatorTargetDTO getOne(IndicatorTargetFilterDTO filter);

	List<IndicatorTargetDTO> getMany(IndicatorTargetFilterDTO filter);
}
