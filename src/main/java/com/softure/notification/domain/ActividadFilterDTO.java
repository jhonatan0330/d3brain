package com.softure.notification.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;
@Alias("ActividadFilterDTO")
public class ActividadFilterDTO extends BasicFilterDTO
{

	private String responsable;
	private String responsableIdentificacion;
	private String responsableNombre;
	private String documento;
	private String responsableFoto;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMax;
	private String usuarioRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInactivoMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInactivoMax;
	private String usuarioInactivo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaLeidoMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaLeidoMax;

	
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}
	
	public String getResponsable() {
		return responsable;
	}
	
	
	public void setResponsableIdentificacion(String responsableIdentificacion) {
		this.responsableIdentificacion = responsableIdentificacion;
	}
	
	public String getResponsableIdentificacion() {
		return responsableIdentificacion;
	}
	
	
	public void setResponsableNombre(String responsableNombre) {
		this.responsableNombre = responsableNombre;
	}
	
	public String getResponsableNombre() {
		return responsableNombre;
	}
	
	
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	
					
	
	public void setResponsableFoto(String responsableFoto) {
		this.responsableFoto = responsableFoto;
	}
	
	public String getResponsableFoto() {
		return responsableFoto;
	}
		
	public void setFechaRegistroMin(Date fechaRegistroMin) {
		this.fechaRegistroMin = fechaRegistroMin;
	}
	
	public Date getFechaRegistroMin() {
		return fechaRegistroMin;
	}
	
	public void setFechaRegistroMax(Date fechaRegistroMax) {
		this.fechaRegistroMax = fechaRegistroMax;
	}
	
	public Date getFechaRegistroMax() {
		return fechaRegistroMax;
	}
	
	
	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}
	
	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}
	
	
	public void setFechaInactivoMin(Date fechaInactivoMin) {
		this.fechaInactivoMin = fechaInactivoMin;
	}
	
	public Date getFechaInactivoMin() {
		return fechaInactivoMin;
	}
	
	public void setFechaInactivoMax(Date fechaInactivoMax) {
		this.fechaInactivoMax = fechaInactivoMax;
	}
	
	public Date getFechaInactivoMax() {
		return fechaInactivoMax;
	}
		
	
	public void setUsuarioInactivo(String usuarioInactivo) {
		this.usuarioInactivo = usuarioInactivo;
	}
	
	public String getUsuarioInactivo() {
		return usuarioInactivo;
	}
	
	
	public void setFechaLeidoMin(Date fechaLeidoMin) {
		this.fechaLeidoMin = fechaLeidoMin;
	}
	
	public Date getFechaLeidoMin() {
		return fechaLeidoMin;
	}
	
	public void setFechaLeidoMax(Date fechaLeidoMax) {
		this.fechaLeidoMax = fechaLeidoMax;
	}
	
	public Date getFechaLeidoMax() {
		return fechaLeidoMax;
	}
	

}