package com.softure.inventory.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("DetalleCaracteristicaProductoDTO")
public class DetalleCaracteristicaProductoDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String entidad;
	private String campo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date valorFecha;
	private String valorText;
	private BigDecimal valorNumero;
	private String valorOpcion;
	private String transaccionRegistro;
	private String transaccionInactivo;

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}
	
	public String getEntidad() {
		return entidad;
	}
	public void setCampo(String campo) {
		this.campo = campo;
	}
	
	public String getCampo() {
		return campo;
	}
	public void setValorFecha(Date valorFecha) {
		this.valorFecha = valorFecha;
	}
	
	public Date getValorFecha() {
		return valorFecha;
	}
	public void setValorText(String valorText) {
		this.valorText = valorText;
	}
	
	public String getValorText() {
		return valorText;
	}
	public void setValorNumero(BigDecimal valorNumero) {
		this.valorNumero = valorNumero;
	}
	
	public BigDecimal getValorNumero() {
		return valorNumero;
	}
	public void setValorOpcion(String valorOpcion) {
		this.valorOpcion = valorOpcion;
	}
	
	public String getValorOpcion() {
		return valorOpcion;
	}
	public void setTransaccionRegistro(String transaccionRegistro) {
		this.transaccionRegistro = transaccionRegistro;
	}
	
	public String getTransaccionRegistro() {
		return transaccionRegistro;
	}
	public void setTransaccionInactivo(String transaccionInactivo) {
		this.transaccionInactivo = transaccionInactivo;
	}
	
	public String getTransaccionInactivo() {
		return transaccionInactivo;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}