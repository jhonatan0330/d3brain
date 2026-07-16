package com.softure.webservice.infrastructure;

import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.webservice.domain.WebServiceEjecucionDTO;
import com.softure.webservice.domain.WebServiceEjecucionFilterDTO;

@SoftureSqlConnMapper(value = "WebServiceEjecucionMapper")
public interface WebServiceEjecucionMapper extends IBasicMapper<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO> {

	List<WebServiceEjecucionDTO> apisTransaccion();
}