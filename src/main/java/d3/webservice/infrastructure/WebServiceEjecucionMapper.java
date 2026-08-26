package d3.webservice.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.webservice.domain.WebServiceEjecucionDTO;
import d3.webservice.domain.WebServiceEjecucionFilterDTO;

@D3SqlConnMapper(value = "WebServiceEjecucionMapper")
public interface WebServiceEjecucionMapper extends IBasicMapper<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO> {

	List<WebServiceEjecucionDTO> apisTransaccion();
}