package com.softure.tariff.infrastructure;

import java.util.List;

import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper("TarifarioSoftureMapper")
public interface TarifarioMapper {

	TarifarioDTO insert(TarifarioDTO dto);

	TarifarioDTO update(TarifarioDTO dto);

	int count(TarifarioFilterDTO filter);
	
	TarifarioDTO getOne(TarifarioFilterDTO filter);

	List<TarifarioDTO> getMany(TarifarioFilterDTO filter);

}