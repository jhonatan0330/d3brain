package d3.users.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.users.domain.ServidorDTO;
import d3.users.domain.ServidorFilterDTO;

@D3SqlConnMapper(value = "ServidorMapper")
public interface ServidorMapper extends IBasicMapper<ServidorDTO, ServidorFilterDTO> {

}