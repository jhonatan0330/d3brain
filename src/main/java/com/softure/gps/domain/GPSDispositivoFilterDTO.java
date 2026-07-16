package com.softure.gps.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("GPSDispositivoFilterDTO")
public class GPSDispositivoFilterDTO extends BasicFilterDTO {

	private String usuario;
	private String nombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date ultimaConexionMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date ultimaConexionMax;
	private Integer intervalo;
	private Integer distancia;
	private Integer acercamiento;
	private String usuarioNombre;

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setUltimaConexionMin(Date ultimaConexionMin) {
		this.ultimaConexionMin = ultimaConexionMin;
	}

	public Date getUltimaConexionMin() {
		return ultimaConexionMin;
	}

	public void setUltimaConexionMax(Date ultimaConexionMax) {
		this.ultimaConexionMax = ultimaConexionMax;
	}

	public Date getUltimaConexionMax() {
		return ultimaConexionMax;
	}

	public void setIntervalo(Integer intervalo) {
		this.intervalo = intervalo;
	}

	public Integer getIntervalo() {
		return intervalo;
	}

	public void setDistancia(Integer distancia) {
		this.distancia = distancia;
	}

	public Integer getDistancia() {
		return distancia;
	}

	public void setAcercamiento(Integer acercamiento) {
		this.acercamiento = acercamiento;
	}

	public Integer getAcercamiento() {
		return acercamiento;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

}