package d3.usage.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicDTO;

@Alias("MovimientoConsumoDTO")
public class MovimientoConsumoDTO extends BasicDTO {

	private String tipo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEvento;
	private BigDecimal cantidad;
	private BigDecimal saldoInicial;
	private BigDecimal saldoFinal;
	private String anterior;
	private String siguiente;
	private String referencia;

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public Date getFechaEvento() {
		return fechaEvento;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setSaldoInicial(BigDecimal saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	public BigDecimal getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoFinal(BigDecimal saldoFinal) {
		this.saldoFinal = saldoFinal;
	}

	public BigDecimal getSaldoFinal() {
		return saldoFinal;
	}

	public void setAnterior(String anterior) {
		this.anterior = anterior;
	}

	public String getAnterior() {
		return anterior;
	}

	public void setSiguiente(String siguiente) {
		this.siguiente = siguiente;
	}

	public String getSiguiente() {
		return siguiente;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getReferencia() {
		return referencia;
	}

}