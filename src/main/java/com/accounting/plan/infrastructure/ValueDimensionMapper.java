package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.plan.domain.ValueDimensionDTO;
import com.accounting.plan.domain.ValueDimensionFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "ValueDimensionAccountingMapper")
public interface ValueDimensionMapper {

	ValueDimensionDTO insert(ValueDimensionDTO dto);

	ValueDimensionDTO update(ValueDimensionDTO dto);

	int count(ValueDimensionFilterDTO filter);
	
	ValueDimensionDTO getOne(ValueDimensionFilterDTO filter);

	List<ValueDimensionDTO> getMany(ValueDimensionFilterDTO filter);

}