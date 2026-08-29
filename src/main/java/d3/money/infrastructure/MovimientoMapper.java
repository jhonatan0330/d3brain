package d3.money.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.money.domain.MovimientoDTO;
import d3.money.domain.MovimientoFilterDTO;

@D3SqlConnMapper(value = "MovimientoMapper")
public interface MovimientoMapper extends IBasicMapper<MovimientoDTO, MovimientoFilterDTO> {

	List<MovimientoDTO> obtenerMovimientoAnteriorFecha(MovimientoFilterDTO dto);

	List<MovimientoDTO> obtenerMovimientoSiguienteFecha(MovimientoFilterDTO dto);

}