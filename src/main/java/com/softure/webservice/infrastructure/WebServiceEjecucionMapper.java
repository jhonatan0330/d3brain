package com.softure.webservice.infrastructure;


// BEGIN region interImport  
import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.webservice.domain.WebServiceEjecucionDTO;
import com.softure.webservice.domain.WebServiceEjecucionFilterDTO;

public interface WebServiceEjecucionMapper extends IBasicMapper<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<WebServiceEjecucionDTO> apisTransaccion();
	int hasPropertiesAsync();
// END region aditionalMethods
}