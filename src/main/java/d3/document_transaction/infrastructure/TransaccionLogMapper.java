package d3.document_transaction.infrastructure;

import d3.D3SqlConnMapper;
import d3.document_transaction.domain.TransaccionLogDTO;
import d3.document_transaction.domain.TransaccionLogFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "TransaccionLogMapper")
public interface TransaccionLogMapper extends IBasicMapper<TransaccionLogDTO, TransaccionLogFilterDTO> {

}