package com.softure.process_form.infrastructure;


import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;

public interface DocumentoPlantillaCaracteristicaMapper extends IBasicMapper<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO>{
	

// BEGIN region aditionalMethods 
	void actualizarFiltros(String llaveTabla);
	List<DocumentoPlantillaCaracteristicaDTO> getFullToSynchronize();
// END region aditionalMethods
}