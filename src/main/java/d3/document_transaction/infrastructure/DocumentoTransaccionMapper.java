package d3.document_transaction.infrastructure;

import d3.D3SqlConnMapper;
import d3.document_transaction.domain.DocumentoTransaccionDTO;
import d3.document_transaction.domain.DocumentoTransaccionFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "DocumentoTransaccionMapper")
public interface DocumentoTransaccionMapper
		extends IBasicMapper<DocumentoTransaccionDTO, DocumentoTransaccionFilterDTO> {

}