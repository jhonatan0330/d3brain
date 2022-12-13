package com.softure.survey.infrastructure;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.survey.domain.EncuestaPreguntaDTO;
import com.softure.survey.domain.EncuestaPreguntaFilterDTO;

public interface EncuestaPreguntaMapper extends IBasicMapper<EncuestaPreguntaDTO, EncuestaPreguntaFilterDTO>{
	

	List<EncuestaPreguntaDTO> listarPermitidas(EncuestaPreguntaFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}