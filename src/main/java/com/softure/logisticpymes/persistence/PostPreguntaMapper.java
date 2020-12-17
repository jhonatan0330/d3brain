package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.PostPreguntaDTO;
import com.softure.logisticpymes.dto.filter.PostPreguntaFilterDTO;

public interface PostPreguntaMapper extends IBasicMapper<PostPreguntaDTO, PostPreguntaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PostPreguntaDTO> listarEnOrden(PostPreguntaFilterDTO dto);
	List<PostPreguntaDTO> listarPreguntasSinRespuesta(PostPreguntaFilterDTO dto);
// END region aditionalMethods
}