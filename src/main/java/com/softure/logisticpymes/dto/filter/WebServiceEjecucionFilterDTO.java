package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("WebServiceEjecucionFilterDTO")
public class WebServiceEjecucionFilterDTO extends BasicFilterDTO
{

	private String servicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String documento;
	private String entrada;
	private String salida;
	private String usuario;
	private String masivo;

	
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	
	public String getServicio() {
		return servicio;
	}
	
	
	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}
	
	public Date getFechaMin() {
		return fechaMin;
	}
	
	public void setFechaMax(Date fechaMax) {
		this.fechaMax = fechaMax;
	}
	
	public Date getFechaMax() {
		return fechaMax;
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
	
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	
	public void setMasivo(String masivo) {
		this.masivo = masivo;
	}
	
	public String getMasivo() {
		return masivo;
	}
	

}