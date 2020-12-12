package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
import org.apache.ibatis.annotations.Param;
// END region interImport
import com.softure.logisticpymes.dto.DocumentoRelacionGestorDTO;
import com.softure.logisticpymes.dto.filter.DocumentoRelacionGestorFilterDTO;

public interface DocumentoRelacionGestorMapper extends IBasicMapper<DocumentoRelacionGestorDTO, DocumentoRelacionGestorFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DocumentoRelacionGestorDTO> listarExpedientesGestionadores(@Param("dto")DocumentoRelacionGestorFilterDTO dto
			, @Param("verAsignacion")String verAsignacion, @Param("verMensajes")String verMensajes
			, @Param("verInventarios")String verInventarios);
	DocumentoRelacionGestorDTO ultimoRegistro(String documentoPrincipal);
// END region aditionalMethods
}