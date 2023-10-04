package com.softure.massiveload.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.massiveload.domain.MassiveItemDTO;
import com.softure.massiveload.domain.MassiveItemFilter;
import com.softure.shared.infrastructure.SharedCRUDMapperMybatis;

@SoftureSqlConnMapper("MassiveItemMapper")
public interface MassiveItemMapper extends SharedCRUDMapperMybatis<MassiveItemDTO, MassiveItemFilter>{
	
}
