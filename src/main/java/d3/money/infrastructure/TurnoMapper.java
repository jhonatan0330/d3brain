package d3.money.infrastructure;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.money.domain.TurnoDTO;
import d3.money.domain.TurnoFilterDTO;

@D3SqlConnMapper(value = "TurnoMapper")
public interface TurnoMapper extends IBasicMapper<TurnoDTO, TurnoFilterDTO> {

}