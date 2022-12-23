package com.softure.webservice.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Alias("WebServiceEjecucionDTO")
public class WebServiceEjecucionDTO extends BasicDTO
{

	private String servicio;
	private String usuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String documento;
	private String modificador;
	private String transaccion;
	private String parametros;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEjecucion;
	private String entrada;
	private String salida;
	private String error;
	private String masivo;
	private String extracciones;
	private String textoRespuesta;
	private String sincrona;
	
}