package d3.document_execution.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document_execution.domain.DocumentoRelacionExpedienteDTO;
import d3.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "DocumentoRelacionExpedienteMapper")
public interface DocumentoRelacionExpedienteMapper
		extends IBasicMapper<DocumentoRelacionExpedienteDTO, DocumentoRelacionExpedienteFilterDTO> {

	List<DocumentoRelacionExpedienteDTO> listarHeredados(@Param("plantilla") String plantilla,
			@Param("campoMaestro") String campoMaestro, @Param("llaveOpcion") String llaveOpcion,
			@Param("plantillaTransicion") String plantillaTransicion);
}