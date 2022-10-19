package com.softure.logisticpymes.domain.filter;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;
@Alias("MensajeFilterDTO")
public class MensajeFilterDTO extends BasicFilterDTO
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;
	private String titulo;
	private String usuario;
	private String documento;
	private String template;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date leidoMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date leidoMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date correoEnviadoMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date correoEnviadoMax;
	private String reporte;
	private String transaccion;

	
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
	
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
	
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	
	
	public void setTemplate(String template) {
		this.template = template;
	}
	
	public String getTemplate() {
		return template;
	}
	
	
	public void setLeidoMin(Date leidoMin) {
		this.leidoMin = leidoMin;
	}
	
	public Date getLeidoMin() {
		return leidoMin;
	}
	
	public void setLeidoMax(Date leidoMax) {
		this.leidoMax = leidoMax;
	}
	
	public Date getLeidoMax() {
		return leidoMax;
	}
	
	
	public void setCorreoEnviadoMin(Date correoEnviadoMin) {
		this.correoEnviadoMin = correoEnviadoMin;
	}
	
	public Date getCorreoEnviadoMin() {
		return correoEnviadoMin;
	}
	
	public void setCorreoEnviadoMax(Date correoEnviadoMax) {
		this.correoEnviadoMax = correoEnviadoMax;
	}
	
	public Date getCorreoEnviadoMax() {
		return correoEnviadoMax;
	}
	
	
	public void setReporte(String reporte) {
		this.reporte = reporte;
	}
	
	public String getReporte() {
		return reporte;
	}
	
	
	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}
	
	public String getTransaccion() {
		return transaccion;
	}
	

}