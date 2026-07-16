package com.softure.money.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("TurnoFilterDTO")
public class TurnoFilterDTO extends BasicFilterDTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaAperturaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaAperturaMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEntregaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEntregaMax;
	private String documento;
	private String usuario;
	private String usuarioNombre;
	private String cuenta;
	private String cuentaNombre;

	public void setFechaAperturaMin(Date fechaAperturaMin) {
		this.fechaAperturaMin = fechaAperturaMin;
	}

	public Date getFechaAperturaMin() {
		return fechaAperturaMin;
	}

	public void setFechaAperturaMax(Date fechaAperturaMax) {
		this.fechaAperturaMax = fechaAperturaMax;
	}

	public Date getFechaAperturaMax() {
		return fechaAperturaMax;
	}

	public void setFechaEntregaMin(Date fechaEntregaMin) {
		this.fechaEntregaMin = fechaEntregaMin;
	}

	public Date getFechaEntregaMin() {
		return fechaEntregaMin;
	}

	public void setFechaEntregaMax(Date fechaEntregaMax) {
		this.fechaEntregaMax = fechaEntregaMax;
	}

	public Date getFechaEntregaMax() {
		return fechaEntregaMax;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuarioNombre(String usuarioNombre) {
		this.usuarioNombre = usuarioNombre;
	}

	public String getUsuarioNombre() {
		return usuarioNombre;
	}

	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	public String getCuenta() {
		return cuenta;
	}

	public void setCuentaNombre(String cuentaNombre) {
		this.cuentaNombre = cuentaNombre;
	}

	public String getCuentaNombre() {
		return cuentaNombre;
	}

}