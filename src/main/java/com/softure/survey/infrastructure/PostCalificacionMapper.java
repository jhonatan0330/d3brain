package com.softure.survey.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.survey.domain.PostCalificacionDTO;
import com.softure.survey.domain.PostCalificacionFilterDTO;

@SoftureSqlConnMapper("PostCalificacionMapper")
public interface PostCalificacionMapper extends IBasicMapper<PostCalificacionDTO, PostCalificacionFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}