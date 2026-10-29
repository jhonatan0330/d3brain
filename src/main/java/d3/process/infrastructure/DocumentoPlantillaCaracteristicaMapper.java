package d3.process.infrastructure;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaFilterDTO;

@D3SqlConnMapper(value = "DocumentoPlantillaCaracteristicaMapper")
public interface DocumentoPlantillaCaracteristicaMapper
		extends IBasicMapper<DocumentoPlantillaCaracteristicaDTO, DocumentoPlantillaCaracteristicaFilterDTO> {

	void actualizarFiltros(String llaveTabla);

	void actualizarDescripcion(@Param("pTemplate") String pTemplate, @Param("pField") String pField);

	int countDependentsOfField(DocumentoPlantillaCaracteristicaFilterDTO pFilter);

}