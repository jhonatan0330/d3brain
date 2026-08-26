package d3.document_transition.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document_transition.domain.DocumentoRelacionGestorDTO;
import d3.document_transition.domain.DocumentoRelacionGestorFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "DocumentoRelacionGestorMapper")
public interface DocumentoRelacionGestorMapper
		extends IBasicMapper<DocumentoRelacionGestorDTO, DocumentoRelacionGestorFilterDTO> {

	List<DocumentoRelacionGestorDTO> listarExpedientesGestionadores(@Param("dto") DocumentoRelacionGestorFilterDTO dto,
			@Param("actual") String historico, @Param("verAsignacion") String verAsignacion,
			@Param("verMensajes") String verMensajes, @Param("verInventarios") String verInventarios,
			@Param("verReportes") String verReportes, @Param("verApi") String verApi,
			@Param("verValores") String verValores, @Param("verUbicacion") String verUbicacion,
			@Param("verComprobantes") String verComprobantes);

	DocumentoRelacionGestorDTO ultimoRegistro(@Param("documentoPrincipal") String documentoPrincipal,
			@Param("historico") String historico);

	String getSystemUser();

	String isActual(String documento);

	DocumentoRelacionGestorDTO insertHistoricTable(DocumentoRelacionGestorDTO dto);

	DocumentoRelacionGestorDTO actualizarHistoricTable(DocumentoRelacionGestorDTO dto);
}