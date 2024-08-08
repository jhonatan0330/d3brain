package com.softure.survey.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.survey.domain.EncuestaGrupoDTO;
import com.softure.survey.domain.EncuestaGrupoFilterDTO;

@SoftureSqlConnMapper(value = "EncuestaGrupoMapper")
public interface EncuestaGrupoMapper extends IBasicMapper<EncuestaGrupoDTO, EncuestaGrupoFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}