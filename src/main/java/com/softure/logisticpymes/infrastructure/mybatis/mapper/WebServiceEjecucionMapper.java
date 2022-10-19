package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.WebServiceEjecucionDTO;
import com.softure.logisticpymes.domain.filter.WebServiceEjecucionFilterDTO;

public interface WebServiceEjecucionMapper extends IBasicMapper<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<WebServiceEjecucionDTO> apisTransaccion();
	int hasPropertiesAsync();
// END region aditionalMethods
}