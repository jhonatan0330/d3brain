package d3.usage.infrastructure;

import d3.D3SqlConnMapper;
import d3.usage.domain.MovimientoConsumoDTO;
import d3.usage.domain.MovimientoConsumoFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "MovimientoConsumoMapper")
public interface MovimientoConsumoMapper
		extends IBasicMapper<MovimientoConsumoDTO, MovimientoConsumoFilterDTO> {

}