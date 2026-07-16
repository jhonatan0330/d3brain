package com.softure.process_form.domain;

import java.util.List;

import org.apache.ibatis.type.Alias;

import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.domain.BasicFilterDTO;

@Alias("DocumentoPlantillaCaracteristicaFilterDTO")
public class DocumentoPlantillaCaracteristicaFilterDTO extends BasicFilterDTO {

	private String plantilla;
	private String plantillaNombre;
	private String formato;
	private String nombre;
	private String codigo;
	private Integer orden;
	private String imagen;
	private List<PedidoVentaDTO> documentos;

	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}

	public String getPlantilla() {
		return plantilla;
	}

	public void setPlantillaNombre(String plantillaNombre) {
		this.plantillaNombre = plantillaNombre;
	}

	public String getPlantillaNombre() {
		return plantillaNombre;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getFormato() {
		return formato;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}

	public void setDocumentos(List<PedidoVentaDTO> documentos) {
		this.documentos = documentos;
	}

	public List<PedidoVentaDTO> getDocumentos() {
		return documentos;
	}

}