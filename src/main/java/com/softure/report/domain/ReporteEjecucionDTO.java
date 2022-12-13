package com.softure.report.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("ReporteEjecucionDTO")
public class ReporteEjecucionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String reporte;
	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicio;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFin;
	private String error;
	private String usuario;

	public void setReporte(String reporte) {
		this.reporte = reporte;
	}
	
	public String getReporte() {
		return reporte;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	public Date getFechaFin() {
		return fechaFin;
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