package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.ReporteBaseDTO;
import com.softure.logisticpymes.domain.filter.ReporteBaseFilterDTO;

public interface ReporteBaseMapper extends IBasicMapper<ReporteBaseDTO, ReporteBaseFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ReporteBaseDTO> listarMenu();
// END region aditionalMethods
}