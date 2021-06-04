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
			, @Param("actual")String historico
			, @Param("verAsignacion")String verAsignacion
			, @Param("verMensajes")String verMensajes
			, @Param("verInventarios")String verInventarios
			, @Param("verReportes")String verReportes
			, @Param("usuarioAutomatico")String usuarioAutomatico
			, @Param("verApi")String verApi);
	DocumentoRelacionGestorDTO ultimoRegistro(String documentoPrincipal);
	String getSystemUser();
	String isActual(String documento);
	DocumentoRelacionGestorDTO insertHistoricTable(DocumentoRelacionGestorDTO dto);
// END region aditionalMethods
}