package com.softure.massiveload.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.massiveload.domain.MassiveMasterDTO;
import com.softure.massiveload.domain.MassiveMasterFilter;
import com.softure.shared.infrastructure.SharedCRUDMapperMybatis;

@SoftureSqlConnMapper("MassiveMasterMapper")
public interface MassiveMasterMapper extends SharedCRUDMapperMybatis<MassiveMasterDTO, MassiveMasterFilter>{
	
}
