package d3.document_transition.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("PedidoVentaAjusteDTO")
public class PedidoVentaAjusteDTO extends BasicDTO
{

	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String estadoInicial;
	private String estadoFinal;
	private String motivo;
	private String responsable;

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

	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	public String getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoFinal(String estadoFinal) {
		this.estadoFinal = estadoFinal;
	}

	public String getEstadoFinal() {
		return estadoFinal;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public String getResponsable() {
		return responsable;
	}

}