package com.softure.logisticpymes.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("WebServiceEjecucionDTO")
public class WebServiceEjecucionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String servicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String documento;
	private String entrada;
	private String salida;
	private String error;
	private String usuario;

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	
	public String getServicio() {
		return servicio;
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
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}