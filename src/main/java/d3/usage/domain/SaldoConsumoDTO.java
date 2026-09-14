package d3.usage.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicDTO;

@Alias("SaldoConsumoDTO")
public class SaldoConsumoDTO extends BasicDTO {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaActualizacion;
	private BigDecimal saldo;

	public void setFechaActualizacion(Date fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Date getFechaActualizacion() {
		return fechaActualizacion;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

}