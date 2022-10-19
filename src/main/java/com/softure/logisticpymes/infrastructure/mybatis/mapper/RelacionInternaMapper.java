package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.RelacionInternaDTO;
import com.softure.logisticpymes.domain.filter.RelacionInternaFilterDTO;

public interface RelacionInternaMapper extends IBasicMapper<RelacionInternaDTO, RelacionInternaFilterDTO>{
	

	List<RelacionInternaDTO> listarRelacion(RelacionInternaFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}