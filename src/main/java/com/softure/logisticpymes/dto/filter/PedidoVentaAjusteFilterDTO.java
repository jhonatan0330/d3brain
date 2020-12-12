package com.softure.logisticpymes.dto.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("PedidoVentaAjusteFilterDTO")
public class PedidoVentaAjusteFilterDTO extends BasicFilterDTO
{

	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String estadoInicial;
	private String estadoFinal;
	private String responsable;

	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
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
	
	
	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}
	
	public String getEstadoInicial() {
		return estadoInicial;
	}
	
	
	public void setEstadoFinal(String estadoFinal) {
		this.estadoFinal = estadoFinal;
	}
	
	public String getEstadoFinal() {
		return estadoFinal;
	}
	
	
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
	public String getResponsable() {
		return responsable;
	}
	

}