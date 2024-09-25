package com.softure.document_execution.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.softure.java.domain.BasicFilterDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;

// BEGIN region interImport
import java.math.BigDecimal;
import java.util.List;

// END region interImport

import org.apache.ibatis.type.Alias;
@Alias("PedidoVentaCaracteristicaFilterDTO")
@JsonInclude(Include.NON_NULL)
public class PedidoVentaCaracteristicaFilterDTO extends BasicFilterDTO
{

	private String documento;
	private String campo;
	private DocumentoPlantillaCaracteristicaDTO campoDTO;
	private String valorText;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date valorFechaMin;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date valorFechaMax;
	private String valorOpcion;
	private String valorAuxiliar;
	private BigDecimal valorNumeroMin;
	private BigDecimal valorNumeroMax;
	private List<DetallePedidoVentaDTO> detalles;
	private List<PedidoVentaCaracteristicaDTO> dependientes;
	private List<PedidoVentaDTO> expedientes;
	private String transaccionRegistro;
	private String transaccionInactivo;

	
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
	
	
	public void setValorFechaMin(Date valorFechaMin) {
		this.valorFechaMin = valorFechaMin;
	}
	
	public Date getValorFechaMin() {
		return valorFechaMin;
	}
	
	public void setValorFechaMax(Date valorFechaMax) {
		this.valorFechaMax = valorFechaMax;
	}
	
	public Date getValorFechaMax() {
		return valorFechaMax;
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
	
	
	public void setValorNumeroMin(BigDecimal valorNumeroMin) {
		this.valorNumeroMin = valorNumeroMin;
	}
	
	public BigDecimal getValorNumeroMin() {
		return valorNumeroMin;
	}
	
	public void setValorNumeroMax(BigDecimal valorNumeroMax) {
		this.valorNumeroMax = valorNumeroMax;
	}
	
	public BigDecimal getValorNumeroMax() {
		return valorNumeroMax;
	}
	
					
	
	public void setDetalles(List<DetallePedidoVentaDTO> detalles) {
		this.detalles = detalles;
	}
	
	public List<DetallePedidoVentaDTO> getDetalles() {
		return detalles;
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
	

}