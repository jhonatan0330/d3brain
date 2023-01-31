package com.softure.gps.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("GPSLocalizacionDTO")
public class GPSLocalizacionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String dispositivo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private BigDecimal longitud;
	private BigDecimal latitud;
	private String documento;
	private String codigo;

	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}
	
	public String getDispositivo() {
		return dispositivo;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setLongitud(BigDecimal longitud) {
		this.longitud = longitud;
	}
	
	public BigDecimal getLongitud() {
		return longitud;
	}
	public void setLatitud(BigDecimal latitud) {
		this.latitud = latitud;
	}
	
	public BigDecimal getLatitud() {
		return latitud;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

}