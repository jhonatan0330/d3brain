package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

// END region interImport
import com.softure.logisticpymes.dto.DocumentoRelacionExpedienteDTO;
import com.softure.logisticpymes.dto.filter.DocumentoRelacionExpedienteFilterDTO;

public interface DocumentoRelacionExpedienteMapper extends IBasicMapper<DocumentoRelacionExpedienteDTO, DocumentoRelacionExpedienteFilterDTO>{
	

// BEGIN region aditionalMethods  
	public List<DocumentoRelacionExpedienteDTO> listarHeredados(@Param("plantilla") String plantilla, @Param("campoMaestro") String campoMaestro, @Param("llaveOpcion") String llaveOpcion, @Param("plantillaTransicion") String plantillaTransicion);
// END region aditionalMethods
}