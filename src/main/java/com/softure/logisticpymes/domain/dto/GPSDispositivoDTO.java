package com.softure.logisticpymes.domain.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("GPSDispositivoDTO")
public class GPSDispositivoDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String usuario;
	private String nombre;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date ultimaConexion;
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
	public void setUltimaConexion(Date ultimaConexion) {
		this.ultimaConexion = ultimaConexion;
	}
	
	public Date getUltimaConexion() {
		return ultimaConexion;
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
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}