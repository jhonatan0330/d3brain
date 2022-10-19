package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.ProcesoDTO;
import com.softure.logisticpymes.domain.filter.ProcesoFilterDTO;

public interface ProcesoMapper extends IBasicMapper<ProcesoDTO, ProcesoFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProcesoDTO> noUsoSoloQuitoErrorList();
// END region aditionalMethods
}