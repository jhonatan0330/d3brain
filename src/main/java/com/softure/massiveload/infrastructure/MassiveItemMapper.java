package com.softure.massiveload.infrastructure;

import com.shared.infrastructure.SharedCRUDMapperMybatis;
import com.softure.SoftureSqlConnMapper;
import com.softure.massiveload.domain.MassiveItemDTO;
import com.softure.massiveload.domain.MassiveItemFilter;

@SoftureSqlConnMapper(value = "MassiveItemMapper")
public interface MassiveItemMapper extends SharedCRUDMapperMybatis<MassiveItemDTO, MassiveItemFilter> {

}
