package d3.document_transaction.infrastructure;

import d3.D3SqlConnMapper;
import d3.document_transaction.domain.TransaccionErrorDTO;
import d3.document_transaction.domain.TransaccionErrorFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "TransaccionErrorMapper")
public interface TransaccionErrorMapper extends IBasicMapper<TransaccionErrorDTO, TransaccionErrorFilterDTO> {

}