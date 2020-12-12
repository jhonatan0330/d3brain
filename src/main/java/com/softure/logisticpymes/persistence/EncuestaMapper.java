package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.EncuestaDTO;
import com.softure.logisticpymes.dto.filter.EncuestaFilterDTO;

public interface EncuestaMapper extends IBasicMapper<EncuestaDTO, EncuestaFilterDTO>{
	

	List<EncuestaDTO> listarDisponibles(EncuestaFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}