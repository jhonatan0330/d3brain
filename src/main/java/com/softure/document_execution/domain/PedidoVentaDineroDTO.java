package com.softure.document_execution.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("PedidoVentaDineroDTO")
public class PedidoVentaDineroDTO extends BasicDTO
{

	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private BigDecimal valorTotal;
	private BigDecimal saldo;
	private BigDecimal valorCampo;
	private boolean controlarSaldo;

	public void setDocumento(String documento) {
		this.documento = documento;
	}
	
	public String getDocumento() {
		return documento;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	public Date getFecha() {
		return fecha;
	}
	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	public BigDecimal getValorTotal() {
		return valorTotal;
	}
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	public void setValorCampo(BigDecimal valorCampo) {
		this.valorCampo = valorCampo;
	}
	
	public BigDecimal getValorCampo() {
		return valorCampo;
	}

	public boolean getControlarSaldo() {
		return controlarSaldo;
	}

	public void setControlarSaldo(boolean controlarSaldo) {
		this.controlarSaldo = controlarSaldo;
	}

}