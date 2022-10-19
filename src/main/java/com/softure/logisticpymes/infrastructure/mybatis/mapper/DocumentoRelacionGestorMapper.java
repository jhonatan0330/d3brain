package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

// BEGIN region interImport  
import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.DocumentoRelacionGestorDTO;
import com.softure.logisticpymes.domain.filter.DocumentoRelacionGestorFilterDTO;

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
	DocumentoRelacionGestorDTO ultimoRegistro(@Param("documentoPrincipal")String documentoPrincipal, @Param("historico")String historico);
	String getSystemUser();
	String isActual(String documento);
	DocumentoRelacionGestorDTO insertHistoricTable(DocumentoRelacionGestorDTO dto);
	DocumentoRelacionGestorDTO actualizarHistoricTable(DocumentoRelacionGestorDTO dto);
// END region aditionalMethods
}