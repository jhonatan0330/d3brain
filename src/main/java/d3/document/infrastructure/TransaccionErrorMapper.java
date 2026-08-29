package d3.document.infrastructure;

import d3.D3SqlConnMapper;
import d3.document.domain.TransaccionErrorDTO;
import d3.document.domain.TransaccionErrorFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "TransaccionErrorMapper")
public interface TransaccionErrorMapper extends IBasicMapper<TransaccionErrorDTO, TransaccionErrorFilterDTO> {

}