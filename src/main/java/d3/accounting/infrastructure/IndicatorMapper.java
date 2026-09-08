package d3.accounting.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting.domain.IndicatorDTO;
import d3.accounting.domain.IndicatorFilterDTO;

@D3SqlConnMapper(value = "IndicatorAccountingMapper")
public interface IndicatorMapper {

	IndicatorDTO insert(IndicatorDTO dto);

	IndicatorDTO update(IndicatorDTO dto);

	int count(IndicatorFilterDTO filter);

	IndicatorDTO getOne(IndicatorFilterDTO filter);

	List<IndicatorDTO> getMany(IndicatorFilterDTO filter);

}
