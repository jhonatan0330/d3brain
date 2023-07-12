package com.softure.process_form.infrastructure;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;

public interface DocumentoPlantillaMapper extends IBasicMapper<DocumentoPlantillaDTO, DocumentoPlantillaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DocumentoPlantillaDTO> listarMenu(DocumentoPlantillaFilterDTO dto);
	List<DocumentoPlantillaDTO> getFullToSynchronize();
// END region aditionalMethods

}