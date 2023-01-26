package com.softure.api.domain;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class FilterDocumentVO {

	private String template;
	private String id;
	private String code;
	private String filterText;
	private List<String> states;
	private String active;
	private int page;
	private int size;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date creationDateMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date creationDateMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date dateMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date dateMax;
}
