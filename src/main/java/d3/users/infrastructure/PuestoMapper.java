package d3.users.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.users.domain.PuestoDTO;
import d3.users.domain.PuestoFilterDTO;

@D3SqlConnMapper(value = "PuestoMapper")
public interface PuestoMapper extends IBasicMapper<PuestoDTO, PuestoFilterDTO> {

}