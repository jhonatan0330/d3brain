package d3.document_execution.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("PedidoVentaDineroFilterDTO")
public class PedidoVentaDineroFilterDTO extends BasicFilterDTO {

	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaMax;

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setFechaMin(Date fechaMin) {
		this.fechaMin = fechaMin;
	}

	public Date getFechaMin() {
		return fechaMin;
	}

	public void setFechaMax(Date fechaMax) {
		this.fechaMax = fechaMax;
	}

	public Date getFechaMax() {
		return fechaMax;
	}

}