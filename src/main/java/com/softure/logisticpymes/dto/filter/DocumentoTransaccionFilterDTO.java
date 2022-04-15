package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("DocumentoTransaccionFilterDTO")
public class DocumentoTransaccionFilterDTO extends BasicFilterDTO
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String usuario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMax;
	private String sincronize;

	
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
	
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	
	public void setFechaFinMin(Date fechaFinMin) {
		this.fechaFinMin = fechaFinMin;
	}
	
	public Date getFechaFinMin() {
		return fechaFinMin;
	}
	
	public void setFechaFinMax(Date fechaFinMax) {
		this.fechaFinMax = fechaFinMax;
	}
	
	public Date getFechaFinMax() {
		return fechaFinMax;
	}
	
	
	public void setSincronize(String sincronize) {
		this.sincronize = sincronize;
	}
	
	public String getSincronize() {
		return sincronize;
	}
	

}