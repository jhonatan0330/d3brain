package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
import org.apache.ibatis.annotations.Param;
// END region interImport
import com.softure.logisticpymes.dto.PostPreguntaDTO;
import com.softure.logisticpymes.dto.filter.PostPreguntaFilterDTO;

public interface PostPreguntaMapper extends IBasicMapper<PostPreguntaDTO, PostPreguntaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PostPreguntaDTO> listarPreguntasPlantilla(@Param("dto")PostPreguntaFilterDTO dto, @Param("palabrasClave") List<String> palabrasClave);
	List<PostPreguntaDTO> listarPreguntasSinRespuesta(PostPreguntaFilterDTO dto);
// END region aditionalMethods
}