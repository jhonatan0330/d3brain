package d3.webservice.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.webservice.domain.WebServiceDTO;
import d3.webservice.domain.WebServiceFilterDTO;

@D3SqlConnMapper(value = "WebServiceMapper")
public interface WebServiceMapper extends IBasicMapper<WebServiceDTO, WebServiceFilterDTO> {

	List<WebServiceDTO> getFullToSynchronize(@Param("process") List<String> process);

}