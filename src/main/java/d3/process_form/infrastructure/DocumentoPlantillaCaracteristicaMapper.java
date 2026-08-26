package d3.process_form.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;

@D3SqlConnMapper(value = "DocumentoPlantillaCaracteristicaMapper")
public interface DocumentoPlantillaCaracteristicaMapper
		extends IBasicMapper<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO> {

	void actualizarFiltros(String llaveTabla);

	void actualizarDescripcion(@Param("pTemplate") String pTemplate, @Param("pField") String pField);

	List<DocumentoPlantillaCaracteristicaDTO> getFullToSynchronize(@Param("process") List<String> process);

	int countDependentsOfField(DocumentoPlantillaCaracteristicaFilterDTO pFilter);

}