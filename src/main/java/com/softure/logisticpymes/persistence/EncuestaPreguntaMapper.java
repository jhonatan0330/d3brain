package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.EncuestaPreguntaDTO;
import com.softure.logisticpymes.dto.filter.EncuestaPreguntaFilterDTO;

public interface EncuestaPreguntaMapper extends IBasicMapper<EncuestaPreguntaDTO, EncuestaPreguntaFilterDTO>{
	

	List<EncuestaPreguntaDTO> listarPermitidas(EncuestaPreguntaFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}