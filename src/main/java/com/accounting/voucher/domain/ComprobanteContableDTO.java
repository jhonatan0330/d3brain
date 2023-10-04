package com.accounting.voucher.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("ComprobanteContableDTO")
public class ComprobanteContableDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String codigo;
	private String documento;

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}
	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}