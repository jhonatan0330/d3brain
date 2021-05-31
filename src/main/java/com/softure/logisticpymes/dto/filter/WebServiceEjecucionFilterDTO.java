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
	

}