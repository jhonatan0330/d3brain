package d3.process_form.infrastructure;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.process_form.domain.ConsecutivoDTO;
import d3.process_form.domain.ConsecutivoFilterDTO;

@D3SqlConnMapper(value = "ConsecutivoMapper")
public interface ConsecutivoMapper extends IBasicMapper<ConsecutivoDTO, ConsecutivoFilterDTO> {

	String obtenerPrefijo(String documento);
}