package d3.configuration.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.domain.PropiedadValorDefinidoFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "PropiedadValorDefinidoMapper")
public interface PropiedadValorDefinidoMapper
		extends IBasicMapper<PropiedadValorDefinidoDTO, PropiedadValorDefinidoFilterDTO> {

	List<PropiedadValorDefinidoDTO> listarPorOrigen(PropiedadValorDefinidoFilterDTO dto);

	List<PropiedadValorDefinidoDTO> getFullToSynchronize();
}