package com.softure.survey.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.survey.domain.EncuestaRespuestaDTO;
import com.softure.survey.domain.EncuestaRespuestaFilterDTO;

@SoftureSqlConnMapper("EncuestaRespuestaMapper")
public interface EncuestaRespuestaMapper extends IBasicMapper<EncuestaRespuestaDTO, EncuestaRespuestaFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}