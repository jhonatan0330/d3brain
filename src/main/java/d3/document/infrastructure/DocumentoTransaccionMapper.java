package d3.document.infrastructure;

import d3.D3SqlConnMapper;
import d3.document.domain.DocumentoTransaccionDTO;
import d3.document.domain.DocumentoTransaccionFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "DocumentoTransaccionMapper")
public interface DocumentoTransaccionMapper
		extends IBasicMapper<DocumentoTransaccionDTO, DocumentoTransaccionFilterDTO> {

}