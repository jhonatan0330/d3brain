package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.AccountingSqlConnMapper;
import com.accounting.plan.domain.DimensionDTO;
import com.accounting.plan.domain.DimensionFilterDTO;

@AccountingSqlConnMapper("DimensionAccountingMapper")
public interface DimensionMapper {

	DimensionDTO insert(DimensionDTO dto);

	DimensionDTO update(DimensionDTO dto);

	int count(DimensionFilterDTO filter);
	
	DimensionDTO getOne(DimensionFilterDTO filter);

	List<DimensionDTO> getMany(DimensionFilterDTO filter);

}