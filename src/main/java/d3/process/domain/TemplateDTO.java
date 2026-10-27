package d3.process.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import d3.report.domain.ReporteBaseDTO;
import d3.shared.domain.BasicNodoDTO;

@JsonInclude(Include.NON_NULL)
public class TemplateDTO extends BasicNodoDTO
{

	private String proceso;
	private String consecutivo;

	private List<DocumentoPlantillaCaracteristicaDTO> caracteristicas;
	private List<ProcesoEstadoDTO> estados;
	private List<ReporteBaseDTO> reportes;

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	

	public void setCaracteristicas(List<DocumentoPlantillaCaracteristicaDTO> caracteristicas) {
		this.caracteristicas = caracteristicas;
	}

	public List<DocumentoPlantillaCaracteristicaDTO> getCaracteristicas() {
		return caracteristicas;
	}
	
	public void setEstados(List<ProcesoEstadoDTO> estados) {
		this.estados = estados;
	}

	public List<ProcesoEstadoDTO> getEstados() {
		return estados;
	}

	public void setReportes(List<ReporteBaseDTO> reportes) {
		this.reportes = reportes;
	}

	public List<ReporteBaseDTO> getReportes() {
		return reportes;
	}

	public void setProceso(String proceso) {
		this.proceso = proceso;
	}

	public String getProceso() {
		return proceso;
	}

	public static TemplateDTO fromDocumentoPlantilla(DocumentoPlantillaDTO documento) {
		if (documento == null) {
			return null;
		}

		TemplateDTO template = new TemplateDTO();
		template.setLlaveTabla(documento.getLlaveTabla());
		template.setTipo(documento.getTipo());
		template.setNodoSuperior(documento.getNodoSuperior());
		template.setNombre(documento.getNombre());
		template.setConsecutivo(documento.getConsecutivo());
		template.setImagen(documento.getImagen());
		template.setCodigo(documento.getCodigo());
		template.setProceso(documento.getProceso());
		template.setEstado(documento.getEstado());
		
		return template;
	}

	
}