package d3.document_execution.domain;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import d3.java.domain.BasicDTO;

@Alias("DocumentoRelacionExpedienteDTO")
public class DocumentoRelacionExpedienteDTO extends BasicDTO {

	private String campoMaestro;
	private String expedienteDetalle;
	private String documentoRegistro;
	private String documentoInactivo;
	private BigDecimal valor;

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

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValor() {
		return valor;
	}

}