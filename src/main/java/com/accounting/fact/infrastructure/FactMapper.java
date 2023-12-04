package com.accounting.fact.infrastructure;

import java.util.List;

import com.accounting.AccountingConnMapper;
import com.accounting.fact.domain.FactDTO;
import com.accounting.fact.domain.FactFilterDTO;

@AccountingConnMapper("FactAccountingMapper")
public interface FactMapper {

	FactDTO insert(FactDTO dto);

	FactDTO update(FactDTO dto);

	int count(FactFilterDTO filter);
	
	FactDTO getOne(FactFilterDTO filter);

	List<FactDTO> getMany(FactFilterDTO filter);

}