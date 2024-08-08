package com.softure.webservice.infrastructure;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceFilterDTO;

@SoftureSqlConnMapper(value = "WebServiceMapper")
public interface WebServiceMapper extends IBasicMapper<WebServiceDTO, WebServiceFilterDTO>{

	List<WebServiceDTO> getFullToSynchronize(@Param("process")List<String> process);
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}