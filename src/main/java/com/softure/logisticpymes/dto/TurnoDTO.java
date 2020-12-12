package com.softure.logisticpymes.dto;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("TurnoDTO")
public class TurnoDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{
	public static final String ESTADO_INACTIVO = "I";
	public static final String ESTADO_EJECUCION = "E";
	public static final String ESTADO_ACTIVO = "A";
	public static final String ESTADO_FINALIZADO = "F";

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaApertura;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEntrega;
	private BigDecimal montoInicial;
	private BigDecimal montoFinal;
	private String documento;
	private String usuario;
	private String usuarioNombre;
	private String cuenta;
	private String cuentaNombre;

	public void setFechaApertura(Date fechaApertura) {
		this.fechaApertura = fechaApertura;
	}
	
	public Date getFechaApertura() {
		return fechaApertura;
	}
	public void setFechaEntrega(Date fechaEntrega) {
		this.fechaEntrega = fechaEntrega;
	}
	
	public Date getFechaEntrega() {
		return fechaEntrega;
	}
	public void setMontoInicial(BigDecimal montoInicial) {
		this.montoInicial = montoInicial;
	}
	
	public BigDecimal getMontoInicial() {
		return montoInicial;
	}
	public void setMontoFinal(BigDecimal montoFinal) {
		this.montoFinal = montoFinal;
	}
	
	public BigDecimal getMontoFinal() {
		return montoFinal;
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
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}