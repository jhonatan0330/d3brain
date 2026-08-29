package d3.property.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.property.domain.PropiedadValorDefinidoDTO;
import d3.property.domain.PropiedadValorDefinidoFilterDTO;

@D3SqlConnMapper(value = "PropiedadValorDefinidoMapper")
public interface PropiedadValorDefinidoMapper
		extends IBasicMapper<PropiedadValorDefinidoDTO, PropiedadValorDefinidoFilterDTO> {

	List<PropiedadValorDefinidoDTO> listarPorOrigen(PropiedadValorDefinidoFilterDTO dto);

	List<PropiedadValorDefinidoDTO> getFullToSynchronize();
}