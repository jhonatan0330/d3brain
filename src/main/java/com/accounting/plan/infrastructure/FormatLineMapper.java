package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.plan.domain.FormatLineDTO;
import com.accounting.plan.domain.FormatLineFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper("FormatLineAccountingMapper")
public interface FormatLineMapper {

	FormatLineDTO insert(FormatLineDTO dto);

	FormatLineDTO update(FormatLineDTO dto);

	int count(FormatLineFilterDTO filter);
	
	FormatLineDTO getOne(FormatLineFilterDTO filter);

	List<FormatLineDTO> getMany(FormatLineFilterDTO filter);

}