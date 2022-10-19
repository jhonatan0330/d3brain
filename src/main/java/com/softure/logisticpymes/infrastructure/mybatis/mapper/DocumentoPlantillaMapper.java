package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.domain.filter.DocumentoPlantillaFilterDTO;

public interface DocumentoPlantillaMapper extends IBasicMapper<DocumentoPlantillaDTO, DocumentoPlantillaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DocumentoPlantillaDTO> listarMenu(DocumentoPlantillaFilterDTO dto);
// END region aditionalMethods
}