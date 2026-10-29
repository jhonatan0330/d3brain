package d3.webservice.infrastructure;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.webservice.domain.WebServiceDTO;
import d3.webservice.domain.WebServiceFilterDTO;

@D3SqlConnMapper(value = "WebServiceMapper")
public interface WebServiceMapper extends IBasicMapper<WebServiceDTO, WebServiceFilterDTO> {

}