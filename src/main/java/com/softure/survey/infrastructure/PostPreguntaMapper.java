package com.softure.survey.infrastructure;

import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.survey.domain.PostPreguntaDTO;
import com.softure.survey.domain.PostPreguntaFilterDTO;

@SoftureSqlConnMapper(value = "PostPreguntaMapper")
public interface PostPreguntaMapper extends IBasicMapper<PostPreguntaDTO, PostPreguntaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PostPreguntaDTO> listarEnOrden(PostPreguntaFilterDTO dto);
	List<PostPreguntaDTO> listarPreguntasSinRespuesta(PostPreguntaFilterDTO dto);
// END region aditionalMethods
}