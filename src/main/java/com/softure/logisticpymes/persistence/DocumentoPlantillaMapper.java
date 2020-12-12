package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaFilterDTO;

public interface DocumentoPlantillaMapper extends IBasicMapper<DocumentoPlantillaDTO, DocumentoPlantillaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DocumentoPlantillaDTO> listarMenu(DocumentoPlantillaFilterDTO dto);
// END region aditionalMethods
}