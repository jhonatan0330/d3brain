package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.plan.domain.TypeDTO;
import com.accounting.plan.domain.TypeFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "TypeAccountingMapper")
public interface TypeMapper {

	TypeDTO insert(TypeDTO dto);

	TypeDTO update(TypeDTO dto);

	int count(TypeFilterDTO filter);
	
	TypeDTO getOne(TypeFilterDTO filter);

	List<TypeDTO> getMany(TypeFilterDTO filter);

}