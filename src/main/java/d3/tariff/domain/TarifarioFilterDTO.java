package d3.tariff.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import d3.shared.domain.SharedDataObjectFilter;

@Alias("TarifarioFilterDTO")
public class TarifarioFilterDTO extends SharedDataObjectFilter {

	private String nombre;
	private Date fechaInicialMin;
	private Date fechaInicialMax;
	private Date fechaFinalMin;
	private Date fechaFinalMax;
	private String documento;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFechaInicialMin() {
		return fechaInicialMin;
	}

	public void setFechaInicialMin(Date fechaInicialMin) {
		this.fechaInicialMin = fechaInicialMin;
	}

	public Date getFechaInicialMax() {
		return fechaInicialMax;
	}

	public void setFechaInicialMax(Date fechaInicialMax) {
		this.fechaInicialMax = fechaInicialMax;
	}

	public Date getFechaFinalMin() {
		return fechaFinalMin;
	}

	public void setFechaFinalMin(Date fechaFinalMin) {
		this.fechaFinalMin = fechaFinalMin;
	}

	public Date getFechaFinalMax() {
		return fechaFinalMax;
	}

	public void setFechaFinalMax(Date fechaFinalMax) {
		this.fechaFinalMax = fechaFinalMax;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

}