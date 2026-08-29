package d3.process.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.process.domain.ConsecutivoDTO;
import d3.process.domain.ConsecutivoFilterDTO;

@D3SqlConnMapper(value = "ConsecutivoMapper")
public interface ConsecutivoMapper extends IBasicMapper<ConsecutivoDTO, ConsecutivoFilterDTO> {

	String obtenerPrefijo(String documento);
}