package d3.usage.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferenciaConsumoDTO {

	private String tenantDestino;
	private BigDecimal cantidad;
	private String unidad;
	private String referencia;

	public void setTenantDestino(String tenantDestino) {
		this.tenantDestino = tenantDestino;
	}

	public String getTenantDestino() {
		return tenantDestino;
	}

	public void setCantidad(BigDecimal cantidad) {
		this.cantidad = cantidad;
	}

	public BigDecimal getCantidad() {
		return cantidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getReferencia() {
		return referencia;
	}

}
