package com.softure.gps.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import lombok.Getter;
import lombok.Setter;

import org.apache.ibatis.type.Alias;

@Alias("GPSLocalizacionDTO")
@Getter
@Setter
public class GPSLocalizacionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String dispositivo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private BigDecimal longitud;
	private BigDecimal latitud;
	private String documento;
	private String codigo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaReporte;

	
	
}