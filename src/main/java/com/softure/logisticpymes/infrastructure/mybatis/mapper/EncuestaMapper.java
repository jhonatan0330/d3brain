package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.EncuestaDTO;
import com.softure.logisticpymes.domain.filter.EncuestaFilterDTO;

public interface EncuestaMapper extends IBasicMapper<EncuestaDTO, EncuestaFilterDTO>{
	

	List<EncuestaDTO> listarDisponibles(EncuestaFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}