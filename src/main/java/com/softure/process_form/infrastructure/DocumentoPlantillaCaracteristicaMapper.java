package com.softure.process_form.infrastructure;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;

@SoftureSqlConnMapper("DocumentoPlantillaCaracteristicaMapper")
public interface DocumentoPlantillaCaracteristicaMapper extends IBasicMapper<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO>{
	

// BEGIN region aditionalMethods 
	void actualizarFiltros(String llaveTabla);
	List<DocumentoPlantillaCaracteristicaDTO> getFullToSynchronize(@Param("process") List<String> process);
// END region aditionalMethods
}