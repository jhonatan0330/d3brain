package com.softure.survey.infrastructure;

import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.survey.domain.EncuestaDTO;
import com.softure.survey.domain.EncuestaFilterDTO;

@SoftureSqlConnMapper(value = "EncuestaMapper")
public interface EncuestaMapper extends IBasicMapper<EncuestaDTO, EncuestaFilterDTO>{
	

	List<EncuestaDTO> listarDisponibles(EncuestaFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}