package com.softure.inventory.domain;

import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.logisticpymes.domain.BasicParamDTO;

@Alias("ProductoDTO")
public class ProductoDTO extends BasicParamDTO {

	private String nombre;
	private String codigo;
	private String filtros;
	private String imagen;
	private String descripcion;
	private String categoria;
	private String categoriaNombre;
	private String usuarioRol;
	private BigDecimal valorMinimoPromocion;
	private Integer cantidadPromocion;
	private Integer cantidadPromocionBase;
	private DetallePedidoVentaDTO detallePlantilla;
	private String documento;
	private String productoBase;
	private String baseNombre;
	private String templateFields;

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

	public void setFiltros(String filtros) {
		this.filtros = filtros;
	}

	public String getFiltros() {
		return filtros;
	}

	public void setImagen(String imagen) {
		this.imagen = imagen;
	}

	public String getImagen() {
		return imagen;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoriaNombre(String categoriaNombre) {
		this.categoriaNombre = categoriaNombre;
	}

	public String getCategoriaNombre() {
		return categoriaNombre;
	}

	public void setUsuarioRol(String usuarioRol) {
		this.usuarioRol = usuarioRol;
	}

	public String getUsuarioRol() {
		return usuarioRol;
	}

	public void setValorMinimoPromocion(BigDecimal valorMinimoPromocion) {
		this.valorMinimoPromocion = valorMinimoPromocion;
	}

	public BigDecimal getValorMinimoPromocion() {
		return valorMinimoPromocion;
	}

	public void setCantidadPromocion(Integer cantidadPromocion) {
		this.cantidadPromocion = cantidadPromocion;
	}

	public Integer getCantidadPromocion() {
		return cantidadPromocion;
	}

	public void setCantidadPromocionBase(Integer cantidadPromocionBase) {
		this.cantidadPromocionBase = cantidadPromocionBase;
	}

	public Integer getCantidadPromocionBase() {
		return cantidadPromocionBase;
	}

	public void setDetallePlantilla(DetallePedidoVentaDTO detallePlantilla) {
		this.detallePlantilla = detallePlantilla;
	}

	public DetallePedidoVentaDTO getDetallePlantilla() {
		return detallePlantilla;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setProductoBase(String productoBase) {
		this.productoBase = productoBase;
	}

	public String getProductoBase() {
		return productoBase;
	}

	public void setBaseNombre(String baseNombre) {
		this.baseNombre = baseNombre;
	}

	public String getBaseNombre() {
		return baseNombre;
	}

	public void setTemplateFields(String templateFields) {
		this.templateFields = templateFields;
	}

	public String getTemplateFields() {
		return templateFields;
	}

}