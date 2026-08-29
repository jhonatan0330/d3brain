package d3.accounting.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting.domain.TimeFrameDTO;
import d3.accounting.domain.TimeFrameFilterDTO;

@D3SqlConnMapper(value = "TimeFrameAccountingMapper")
public interface TimeFrameMapper {

	TimeFrameDTO insert(TimeFrameDTO dto);

	TimeFrameDTO update(TimeFrameDTO dto);

	int count(TimeFrameFilterDTO filter);

	TimeFrameDTO getOne(TimeFrameFilterDTO filter);

	List<TimeFrameDTO> getMany(TimeFrameFilterDTO filter);

}