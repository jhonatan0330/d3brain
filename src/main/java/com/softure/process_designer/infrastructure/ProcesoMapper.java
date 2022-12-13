package com.softure.process_designer.infrastructure;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;

public interface ProcesoMapper extends IBasicMapper<ProcesoDTO, ProcesoFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProcesoDTO> noUsoSoloQuitoErrorList();
// END region aditionalMethods
}