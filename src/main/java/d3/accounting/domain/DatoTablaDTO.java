package d3.accounting.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;

@Alias("DatoTablaDTO")
public class DatoTablaDTO {

	private String llaveTabla;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String descripcion;
	private BigDecimal valor;

	public String getLlaveTabla() {
		return llaveTabla;
	}

	public void setLlaveTabla(String llaveTabla) {
		this.llaveTabla = llaveTabla;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}