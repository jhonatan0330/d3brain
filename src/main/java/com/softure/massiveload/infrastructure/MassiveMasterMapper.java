package com.softure.massiveload.infrastructure;

import com.shared.infrastructure.SharedCRUDMapperMybatis;
import com.softure.SoftureSqlConnMapper;
import com.softure.massiveload.domain.MassiveMasterDTO;
import com.softure.massiveload.domain.MassiveMasterFilter;

@SoftureSqlConnMapper(value = "MassiveMasterMapper")
public interface MassiveMasterMapper extends SharedCRUDMapperMybatis<MassiveMasterDTO, MassiveMasterFilter> {

}
