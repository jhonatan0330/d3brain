package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaCaracteristicaFilterDTO;

public interface DocumentoPlantillaCaracteristicaMapper extends IBasicMapper<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO>{
	

// BEGIN region aditionalMethods  

	void actualizarFiltros(String llaveTabla);
	
// END region aditionalMethods
}