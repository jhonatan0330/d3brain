package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.PostRespuestaDTO;
import com.softure.logisticpymes.dto.filter.PostRespuestaFilterDTO;

public interface PostRespuestaMapper extends IBasicMapper<PostRespuestaDTO, PostRespuestaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PostRespuestaDTO> listarEnOrden(PostRespuestaFilterDTO dto);
// END region aditionalMethods
}