package com.softure.logisticpymes.infrastructure.mybatis.mapper;


import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.domain.filter.DocumentoPlantillaCaracteristicaFilterDTO;

public interface DocumentoPlantillaCaracteristicaMapper extends IBasicMapper<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO>{
	

// BEGIN region aditionalMethods  

	void actualizarFiltros(String llaveTabla);
	
// END region aditionalMethods
}