package d3.document_execution.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicDTO;

@Alias("PedidoVentaUbicacionDTO")
public class PedidoVentaUbicacionDTO extends BasicDTO {

	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String ubicacion;
	private String modificador;

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

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getModificador() {
		return modificador;
	}

	public void setModificador(String modificador) {
		this.modificador = modificador;
	}

}