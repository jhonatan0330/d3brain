package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;

@AccountingConnMapper("CatalogAccountingMapper")
public interface CatalogMapper {

	CatalogDTO insert(CatalogDTO dto);

	CatalogDTO update(CatalogDTO dto);

	int count(CatalogFilterDTO filter);
	
	CatalogDTO getOne(CatalogFilterDTO filter);

	List<CatalogDTO> getMany(CatalogFilterDTO filter);

}