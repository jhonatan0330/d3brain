package d3.logisticpymes.infrastructure;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.logisticpymes.domain.ServidorDTO;
import d3.logisticpymes.domain.ServidorFilterDTO;

@D3SqlConnMapper(value = "ServidorMapper")
public interface ServidorMapper extends IBasicMapper<ServidorDTO, ServidorFilterDTO> {

}