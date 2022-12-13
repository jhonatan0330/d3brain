package com.softure.inventory.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;
@Alias("DetalleCaracteristicaProductoFilterDTO")
public class DetalleCaracteristicaProductoFilterDTO extends BasicFilterDTO
{

	private String entidad;
	private String campo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date valorFechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date valorFechaMax;
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
	
	
	public void setValorFechaMin(Date valorFechaMin) {
		this.valorFechaMin = valorFechaMin;
	}
	
	public Date getValorFechaMin() {
		return valorFechaMin;
	}
	
	public void setValorFechaMax(Date valorFechaMax) {
		this.valorFechaMax = valorFechaMax;
	}
	
	public Date getValorFechaMax() {
		return valorFechaMax;
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
	

}