package d3.accounting.domain;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicDTO;

@Alias("IndicatorTargetDTO")
public class IndicatorTargetDTO extends BasicDTO {

	private String nombre;
	private BigDecimal valorSuperior;
	private BigDecimal valorInferior;
	private String color;
	private String indicador;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public BigDecimal getValorSuperior() {
		return valorSuperior;
	}

	public void setValorSuperior(BigDecimal valorSuperior) {
		this.valorSuperior = valorSuperior;
	}

	public BigDecimal getValorInferior() {
		return valorInferior;
	}

	public void setValorInferior(BigDecimal valorInferior) {
		this.valorInferior = valorInferior;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}



}
