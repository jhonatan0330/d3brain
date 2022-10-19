package com.softure.logisticpymes.domain.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("WebServiceEjecucionDTO")
public class WebServiceEjecucionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
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

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	
	public String getServicio() {
		return servicio;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	public void setModificador(String modificador) {
		this.modificador = modificador;
	}
	
	public String getModificador() {
		return modificador;
	}
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	
	public String getTransaccion() {
		return transaccion;
	}
	public void setParametros(String parametros) {
		this.parametros = parametros;
	}
	
	public String getParametros() {
		return parametros;
	}
	public void setFechaEjecucion(Date fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}
	
	public Date getFechaEjecucion() {
		return fechaEjecucion;
	}
	public void setEntrada(String entrada) {
		this.entrada = entrada;
	}
	
	public String getEntrada() {
		return entrada;
	}
	public void setSalida(String salida) {
		this.salida = salida;
	}
	
	public String getSalida() {
		return salida;
	}
	public void setError(String error) {
		this.error = error;
	}
	
	public String getError() {
		return error;
	}
	public void setMasivo(String masivo) {
		this.masivo = masivo;
	}
	
	public String getMasivo() {
		return masivo;
	}
	public void setExtracciones(String extracciones) {
		this.extracciones = extracciones;
	}
	
	public String getExtracciones() {
		return extracciones;
	}
	public void setTextoRespuesta(String textoRespuesta) {
		this.textoRespuesta = textoRespuesta;
	}
	
	public String getTextoRespuesta() {
		return textoRespuesta;
	}
	public void setSincrona(String sincrona) {
		this.sincrona = sincrona;
	}
	
	public String getSincrona() {
		return sincrona;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}