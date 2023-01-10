package com.softure.webservice.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("WebServiceFilterDTO")
public class WebServiceFilterDTO extends BasicFilterDTO
{

	private String nombre;
	private String codigo;
	private String url;

}