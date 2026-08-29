package d3.report.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.shared.domain.BasicFilterDTO;

import org.apache.ibatis.type.Alias;

@Alias("ReporteEjecucionFilterDTO")
public class ReporteEjecucionFilterDTO extends BasicFilterDTO {

	private String reporte;
	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInicioMax;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaFinMax;
	private String usuario;
	private String url;

	public void setReporte(String reporte) {
		this.reporte = reporte;
	}

	public String getReporte() {
		return reporte;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setFechaInicioMin(Date fechaInicioMin) {
		this.fechaInicioMin = fechaInicioMin;
	}

	public Date getFechaInicioMin() {
		return fechaInicioMin;
	}

	public void setFechaInicioMax(Date fechaInicioMax) {
		this.fechaInicioMax = fechaInicioMax;
	}

	public Date getFechaInicioMax() {
		return fechaInicioMax;
	}

	public void setFechaFinMin(Date fechaFinMin) {
		this.fechaFinMin = fechaFinMin;
	}

	public Date getFechaFinMin() {
		return fechaFinMin;
	}

	public void setFechaFinMax(Date fechaFinMax) {
		this.fechaFinMax = fechaFinMax;
	}

	public Date getFechaFinMax() {
		return fechaFinMax;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

}