package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.EncuestaPreguntaDTO;
import com.softure.logisticpymes.domain.filter.EncuestaPreguntaFilterDTO;

public interface EncuestaPreguntaMapper extends IBasicMapper<EncuestaPreguntaDTO, EncuestaPreguntaFilterDTO>{
	

	List<EncuestaPreguntaDTO> listarPermitidas(EncuestaPreguntaFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}