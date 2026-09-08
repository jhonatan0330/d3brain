package d3.accounting.domain;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonProperty;

@Alias("IndicadorResultadoDTO")
public class IndicatorResultadoDTO {

	private BigDecimal valor;
	private PeriodoDTO periodo;
	@JsonProperty("valor_antes")
	private BigDecimal valorAntes;
	@JsonProperty("valor_despues")
	private BigDecimal valorDespues;

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public PeriodoDTO getPeriodo() {
		return periodo;
	}

	public void setPeriodo(PeriodoDTO periodo) {
		this.periodo = periodo;
	}

	public BigDecimal getValorAntes() {
		return valorAntes;
	}

	public void setValorAntes(BigDecimal valorAntes) {
		this.valorAntes = valorAntes;
	}

	public BigDecimal getValorDespues() {
		return valorDespues;
	}

	public void setValorDespues(BigDecimal valorDespues) {
		this.valorDespues = valorDespues;
	}

}