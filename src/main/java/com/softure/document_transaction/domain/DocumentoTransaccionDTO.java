package com.softure.document_transaction.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("DocumentoTransaccionDTO")
public class DocumentoTransaccionDTO extends BasicDTO
// BEGIN region interfaces  
// END region interfaces
{

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String usuario;

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	public String getUsuario() {
		return usuario;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}