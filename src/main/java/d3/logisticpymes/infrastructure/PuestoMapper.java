package d3.logisticpymes.infrastructure;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.logisticpymes.domain.PuestoDTO;
import d3.logisticpymes.domain.PuestoFilterDTO;

@D3SqlConnMapper(value = "PuestoMapper")
public interface PuestoMapper extends IBasicMapper<PuestoDTO, PuestoFilterDTO> {

}