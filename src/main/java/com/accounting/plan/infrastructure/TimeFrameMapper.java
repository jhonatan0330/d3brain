package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.plan.domain.TimeFrameDTO;
import com.accounting.plan.domain.TimeFrameFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "TimeFrameAccountingMapper")
public interface TimeFrameMapper {

	TimeFrameDTO insert(TimeFrameDTO dto);

	TimeFrameDTO update(TimeFrameDTO dto);

	int count(TimeFrameFilterDTO filter);
	
	TimeFrameDTO getOne(TimeFrameFilterDTO filter);

	List<TimeFrameDTO> getMany(TimeFrameFilterDTO filter);

}