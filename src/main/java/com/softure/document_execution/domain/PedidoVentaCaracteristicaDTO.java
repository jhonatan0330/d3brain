package com.softure.document_execution.domain;

import java.util.List;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.java.domain.BasicDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;

import org.apache.ibatis.type.Alias;

@Alias("PedidoVentaCaracteristicaDTO")
@JsonInclude(Include.NON_NULL)
public class PedidoVentaCaracteristicaDTO extends BasicDTO {

	private String documento;
	private String campo;
	private DocumentoPlantillaCaracteristicaDTO campoDTO;
	private String valorText;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date valorFecha;
	private String valorOpcion;
	private String valorAuxiliar;
	private BigDecimal valorNumero;
	private PedidoVentaDTO principal;
	private List<DetallePedidoVentaDTO> detalles;
	private List<UsuarioRolProductoDTO> productosExclusivos;
	private List<PedidoVentaCaracteristicaDTO> dependientes;
	private List<PedidoVentaDTO> expedientes;
	private boolean modificado;
	private String transaccionRegistro;
	private String transaccionInactivo;
	private PedidoVentaDTO documentsToBPM;
	private boolean modificadoBPM;
	private PedidoVentaCaracteristicaDTO difference;

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampoDTO(DocumentoPlantillaCaracteristicaDTO campoDTO) {
		this.campoDTO = campoDTO;
	}

	public DocumentoPlantillaCaracteristicaDTO getCampoDTO() {
		return campoDTO;
	}

	public void setValorText(String valorText) {
		this.valorText = valorText;
	}

	public String getValorText() {
		return valorText;
	}

	public void setValorFecha(Date valorFecha) {
		this.valorFecha = valorFecha;
	}

	public Date getValorFecha() {
		return valorFecha;
	}

	public void setValorOpcion(String valorOpcion) {
		this.valorOpcion = valorOpcion;
	}

	public String getValorOpcion() {
		return valorOpcion;
	}

	public void setValorAuxiliar(String valorAuxiliar) {
		this.valorAuxiliar = valorAuxiliar;
	}

	public String getValorAuxiliar() {
		return valorAuxiliar;
	}

	public void setValorNumero(BigDecimal valorNumero) {
		this.valorNumero = valorNumero;
	}

	public BigDecimal getValorNumero() {
		return valorNumero;
	}

	public void setPrincipal(PedidoVentaDTO principal) {
		this.principal = principal;
	}

	public PedidoVentaDTO getPrincipal() {
		return principal;
	}

	public void setDetalles(List<DetallePedidoVentaDTO> detalles) {
		this.detalles = detalles;
	}

	public List<DetallePedidoVentaDTO> getDetalles() {
		return detalles;
	}

	public void setProductosExclusivos(List<UsuarioRolProductoDTO> productosExclusivos) {
		this.productosExclusivos = productosExclusivos;
	}

	public List<UsuarioRolProductoDTO> getProductosExclusivos() {
		return productosExclusivos;
	}

	public void setDependientes(List<PedidoVentaCaracteristicaDTO> dependientes) {
		this.dependientes = dependientes;
	}

	public List<PedidoVentaCaracteristicaDTO> getDependientes() {
		return dependientes;
	}

	public void setExpedientes(List<PedidoVentaDTO> expedientes) {
		this.expedientes = expedientes;
	}

	public List<PedidoVentaDTO> getExpedientes() {
		return expedientes;
	}

	public void setModificado(boolean modificado) {
		this.modificado = modificado;
	}

	public boolean getModificado() {
		return modificado;
	}

	public void setTransaccionRegistro(String transaccionRegistro) {
		this.transaccionRegistro = transaccionRegistro;
	}

	public String getTransaccionRegistro() {
		return transaccionRegistro;
	}

	public void setTransaccionInactivo(String transaccionInactivo) {
		this.transaccionInactivo = transaccionInactivo;
	}

	public String getTransaccionInactivo() {
		return transaccionInactivo;
	}

	public PedidoVentaDTO getDocumentsToBPM() {
		return documentsToBPM;
	}

	public void setDocumentsToBPM(PedidoVentaDTO documentsToBPM) {
		this.documentsToBPM = documentsToBPM;
	}

	public boolean isModificadoBPM() {
		return modificadoBPM;
	}

	public void setModificadoBPM(boolean modificadoBPM) {
		this.modificadoBPM = modificadoBPM;
	}

	public void setDifference(PedidoVentaCaracteristicaDTO difference) {
		this.difference = difference;
	}

	public PedidoVentaCaracteristicaDTO getDifference() {
		return difference;
	}

	public PedidoVentaCaracteristicaDTO clone() {
		PedidoVentaCaracteristicaDTO cloneNew = new PedidoVentaCaracteristicaDTO();
		cloneNew.setDocumento(this.documento);
		cloneNew.setCampo(this.campo);
		cloneNew.setCampoDTO(this.campoDTO);
		cloneNew.setValorText(this.valorText);
		cloneNew.setValorFecha(this.valorFecha);
		cloneNew.setValorOpcion(this.valorOpcion);
		cloneNew.setValorAuxiliar(this.valorAuxiliar);
		cloneNew.setValorNumero(this.valorNumero);
		cloneNew.setPrincipal(this.principal);
		cloneNew.setDetalles(this.detalles);
		cloneNew.setProductosExclusivos(this.productosExclusivos);
		cloneNew.setDependientes(this.dependientes);
		cloneNew.setExpedientes(this.expedientes);
		cloneNew.setModificado(this.modificado);
		cloneNew.setTransaccionRegistro(this.transaccionRegistro);
		cloneNew.setTransaccionInactivo(this.transaccionInactivo);
		return cloneNew;
	}

}