package d3.document.domain;


import org.apache.ibatis.type.Alias;

import d3.shared.domain.BasicFilterDTO;

@Alias("DocumentoRelacionExpedienteFilterDTO")
public class DocumentoRelacionExpedienteFilterDTO extends BasicFilterDTO {

	private String campoMaestro;
	private String expedienteDetalle;
	private String documentoRegistro;
	private String documentoInactivo;

	public void setCampoMaestro(String campoMaestro) {
		this.campoMaestro = campoMaestro;
	}

	public String getCampoMaestro() {
		return campoMaestro;
	}

	public void setExpedienteDetalle(String expedienteDetalle) {
		this.expedienteDetalle = expedienteDetalle;
	}

	public String getExpedienteDetalle() {
		return expedienteDetalle;
	}

	public void setDocumentoRegistro(String documentoRegistro) {
		this.documentoRegistro = documentoRegistro;
	}

	public String getDocumentoRegistro() {
		return documentoRegistro;
	}

	public void setDocumentoInactivo(String documentoInactivo) {
		this.documentoInactivo = documentoInactivo;
	}

	public String getDocumentoInactivo() {
		return documentoInactivo;
	}

}