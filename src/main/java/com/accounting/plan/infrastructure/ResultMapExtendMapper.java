package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.ResultMapDTO;
import com.accounting.plan.domain.ResultMapFilterDTO;

@AccountingConnMapper("ResultMapExtendAccountingMapper")
public interface ResultMapExtendMapper {

	ResultMapDTO insert(ResultMapDTO dto);

	ResultMapDTO update(ResultMapDTO dto);

	int count(ResultMapFilterDTO filter);
	
	ResultMapDTO getOne(ResultMapFilterDTO filter);

	List<ResultMapDTO> getMany(ResultMapFilterDTO filter);

}