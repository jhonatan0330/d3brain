package d3.process.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import d3.report.domain.ReporteBaseDTO;
import d3.shared.domain.BasicParamDTO;

@JsonInclude(Include.NON_NULL)
public class TemplateDTO extends BasicParamDTO
{

	private String tipo;
	private String padre;
	private String nombre;
	private String consecutivo;
	private String imagen;
	private List<DocumentoPlantillaCaracteristicaDTO> caracteristicas;
	private List<ProcesoEstadoDTO> estados;
	private List<ReporteBaseDTO> reportes;
	private String codigo;
	private String proceso;

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setPadre(String padre) {
		this.padre = padre;
	}

	public String getPadre() {
		return padre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getConsecutivo() {
		return consecutivo;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
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

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
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

		template.setTipo(documento.getTipo());
		template.setPadre(documento.getPadre());
		template.setNombre(documento.getNombre());
		template.setConsecutivo(documento.getConsecutivo());
		template.setImagen(documento.getImagen());
		template.setCodigo(documento.getCodigo());
		template.setProceso(documento.getProceso());

		return template;
	}

	
}