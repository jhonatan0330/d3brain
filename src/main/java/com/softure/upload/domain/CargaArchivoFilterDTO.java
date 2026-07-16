package com.softure.upload.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("CargaArchivoFilterDTO")
public class CargaArchivoFilterDTO extends BasicFilterDTO {

	private String servidor;
	private Integer size;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMax;
	private String usuario;

	public void setServidor(String servidor) {
		this.servidor = servidor;
	}

	public String getServidor() {
		return servidor;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public Integer getSize() {
		return size;
	}

	public void setFechaInicioMin(Date fechaInicioMin) {
		this.fechaInicioMin = fechaInicioMin;
	}

	public Date getFechaInicioMin() {
		return fechaInicioMin;
	}

	public void setFechaInicioMax(Date fechaInicioMax) {
		this.fechaInicioMax = fechaInicioMax;
	}

	public Date getFechaInicioMax() {
		return fechaInicioMax;
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

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

}