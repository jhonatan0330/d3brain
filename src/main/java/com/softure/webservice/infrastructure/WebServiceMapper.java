package com.softure.webservice.infrastructure;


import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceFilterDTO;

@SoftureSqlConnMapper("WebServiceMapper")
public interface WebServiceMapper extends IBasicMapper<WebServiceDTO, WebServiceFilterDTO>{

	List<WebServiceDTO> getFullToSynchronize();
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}