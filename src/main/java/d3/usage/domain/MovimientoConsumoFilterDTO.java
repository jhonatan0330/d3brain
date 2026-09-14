package d3.usage.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicFilterDTO;

@Alias("MovimientoConsumoFilterDTO")
public class MovimientoConsumoFilterDTO extends BasicFilterDTO {

	private String tipo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistroMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEventoMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEventoMax;
	private String referencia;

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setFechaRegistroMin(Date fechaRegistroMin) {
		this.fechaRegistroMin = fechaRegistroMin;
	}

	public Date getFechaRegistroMin() {
		return fechaRegistroMin;
	}

	public void setFechaRegistroMax(Date fechaRegistroMax) {
		this.fechaRegistroMax = fechaRegistroMax;
	}

	public Date getFechaRegistroMax() {
		return fechaRegistroMax;
	}

	public void setFechaEventoMin(Date fechaEventoMin) {
		this.fechaEventoMin = fechaEventoMin;
	}

	public Date getFechaEventoMin() {
		return fechaEventoMin;
	}

	public void setFechaEventoMax(Date fechaEventoMax) {
		this.fechaEventoMax = fechaEventoMax;
	}

	public Date getFechaEventoMax() {
		return fechaEventoMax;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getReferencia() {
		return referencia;
	}

}