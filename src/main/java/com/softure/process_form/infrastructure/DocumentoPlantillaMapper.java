package com.softure.process_form.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;

@SoftureSqlConnMapper(value = "DocumentoPlantillaMapper")
public interface DocumentoPlantillaMapper extends IBasicMapper<DocumentoPlantillaDTO, DocumentoPlantillaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DocumentoPlantillaDTO> listarMenu(DocumentoPlantillaFilterDTO dto);
	List<DocumentoPlantillaDTO> getFullToSynchronize(@Param("process") List<String> process);
	DocumentoPlantillaDTO getTemplateConfiguration(String configuration);
// END region aditionalMethods

}