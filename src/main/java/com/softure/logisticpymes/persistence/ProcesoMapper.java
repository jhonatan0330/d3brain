package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.ProcesoDTO;
import com.softure.logisticpymes.dto.filter.ProcesoFilterDTO;

public interface ProcesoMapper extends IBasicMapper<ProcesoDTO, ProcesoFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProcesoDTO> noUsoSoloQuitoErrorList();
// END region aditionalMethods
}