package d3.process_form.infrastructure;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.process_form.domain.PlantillaConsecutivoDTO;
import d3.process_form.domain.PlantillaConsecutivoFilterDTO;

@D3SqlConnMapper(value = "PlantillaConsecutivoMapper")
public interface PlantillaConsecutivoMapper
		extends IBasicMapper<PlantillaConsecutivoDTO, PlantillaConsecutivoFilterDTO> {

}