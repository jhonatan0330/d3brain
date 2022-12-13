package com.softure.survey.infrastructure;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.survey.domain.PostRespuestaDTO;
import com.softure.survey.domain.PostRespuestaFilterDTO;

public interface PostRespuestaMapper extends IBasicMapper<PostRespuestaDTO, PostRespuestaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PostRespuestaDTO> listarEnOrden(PostRespuestaFilterDTO dto);
// END region aditionalMethods
}