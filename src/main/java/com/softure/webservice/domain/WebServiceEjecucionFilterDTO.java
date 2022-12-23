package com.softure.webservice.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import lombok.Getter;
import lombok.Setter;

import org.apache.ibatis.type.Alias;

@Getter
@Setter
@Alias("WebServiceEjecucionFilterDTO")
public class WebServiceEjecucionFilterDTO extends BasicFilterDTO
{

	private String servicio;
	private String usuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String documento;
	private String modificador;
	private String transaccion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEjecucionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEjecucionMax;
	private String entrada;
	private String salida;
	private String masivo;
	private String textoRespuesta;
	private String sincrona;

}