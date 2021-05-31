package com.softure.logisticpymes.dto;

import java.util.List;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

@Alias("DocumentoPlantillaDTO")
public class DocumentoPlantillaDTO extends BasicParamDTO
// BEGIN region interfaces  
// END region interfaces
{

	private String objetivo;
	private String nombre;
	private String consecutivo;
	private String imagen;
	private List<DocumentoPlantillaCaracteristicaDTO> caracteristicas;
	private List<ProcesoEstadoDTO> estados;
	private String color;
	private List<PedidoVentaDTO> documentos;
	private List<ReporteBaseDTO> reportes;
	private String codigo;
	private String server;

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	
	public String getObjetivo() {
		return objetivo;
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
	public void setColor(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	public void setDocumentos(List<PedidoVentaDTO> documentos) {
		this.documentos = documentos;
	}
	
	public List<PedidoVentaDTO> getDocumentos() {
		return documentos;
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
	public void setServer(String server) {
		this.server = server;
	}
	
	public String getServer() {
		return server;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}