package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.DocumentoRelacionExpedienteDTO;
import com.softure.logisticpymes.domain.filter.DocumentoRelacionExpedienteFilterDTO;

public interface DocumentoRelacionExpedienteMapper extends IBasicMapper<DocumentoRelacionExpedienteDTO, DocumentoRelacionExpedienteFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DocumentoRelacionExpedienteDTO> listarHeredados(@Param("plantilla") String plantilla, @Param("campoMaestro") String campoMaestro, @Param("llaveOpcion") String llaveOpcion, @Param("plantillaTransicion") String plantillaTransicion);
// END region aditionalMethods
}