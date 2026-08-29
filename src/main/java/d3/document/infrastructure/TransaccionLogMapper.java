package d3.document.infrastructure;

import d3.D3SqlConnMapper;
import d3.document.domain.TransaccionLogDTO;
import d3.document.domain.TransaccionLogFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "TransaccionLogMapper")
public interface TransaccionLogMapper extends IBasicMapper<TransaccionLogDTO, TransaccionLogFilterDTO> {

}