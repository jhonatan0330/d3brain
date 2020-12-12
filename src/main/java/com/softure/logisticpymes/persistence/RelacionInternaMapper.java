package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.dto.filter.RelacionInternaFilterDTO;

public interface RelacionInternaMapper extends IBasicMapper<RelacionInternaDTO, RelacionInternaFilterDTO>{
	

	List<RelacionInternaDTO> listarRelacion(RelacionInternaFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}