package com.softure.webservice.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.logisticpymes.domain.BasicParamDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("WebServiceDTO")
public class WebServiceDTO extends BasicParamDTO
{

	private String nombre;
	private String codigo;
	private String template;
	private String url;

}