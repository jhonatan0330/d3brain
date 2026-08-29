package d3.process.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.process.domain.PlantillaConsecutivoDTO;
import d3.process.domain.PlantillaConsecutivoFilterDTO;

@D3SqlConnMapper(value = "PlantillaConsecutivoMapper")
public interface PlantillaConsecutivoMapper
		extends IBasicMapper<PlantillaConsecutivoDTO, PlantillaConsecutivoFilterDTO> {

}