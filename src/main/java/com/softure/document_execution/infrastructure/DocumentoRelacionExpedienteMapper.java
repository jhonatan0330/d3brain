package com.softure.document_execution.infrastructure;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "DocumentoRelacionExpedienteMapper")
public interface DocumentoRelacionExpedienteMapper extends IBasicMapper<DocumentoRelacionExpedienteDTO, DocumentoRelacionExpedienteFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DocumentoRelacionExpedienteDTO> listarHeredados(@Param("plantilla") String plantilla, @Param("campoMaestro") String campoMaestro, @Param("llaveOpcion") String llaveOpcion, @Param("plantillaTransicion") String plantillaTransicion);
// END region aditionalMethods
}