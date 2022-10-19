package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.PostPreguntaDTO;
import com.softure.logisticpymes.domain.filter.PostPreguntaFilterDTO;

public interface PostPreguntaMapper extends IBasicMapper<PostPreguntaDTO, PostPreguntaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PostPreguntaDTO> listarEnOrden(PostPreguntaFilterDTO dto);
	List<PostPreguntaDTO> listarPreguntasSinRespuesta(PostPreguntaFilterDTO dto);
// END region aditionalMethods
}