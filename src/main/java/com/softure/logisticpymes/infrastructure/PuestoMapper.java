package com.softure.logisticpymes.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.PuestoDTO;
import com.softure.logisticpymes.domain.PuestoFilterDTO;

@SoftureSqlConnMapper(value = "PuestoMapper")
public interface PuestoMapper extends IBasicMapper<PuestoDTO, PuestoFilterDTO> {

}