package com.softure.tariff.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.softure.java.domain.BasicDTO;

@Alias("TarifaDTO")
public class TarifaDTO extends BasicDTO {

	private String tarifario;
	private String tarifarioNombre;
	private String tarifarioDocumento;
	private String documento;
	private String producto;
	private String productoDocumento;
	private String productoNombre;
	private String recurso;
	private BigDecimal valorMinimo;
	private BigDecimal valor;
	private BigDecimal valorMaximo;
	private Integer cantidadMinima;
	private Integer cantidadMaxima;
	private BigDecimal totalMinimo;
	private String dimension2;
	private String dimension3;
	private String dimension4;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date createdAt;
	private String createdUser;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date updatedAt;
	private String updatedUser;

	public void setTarifario(String tarifario) {
		this.tarifario = tarifario;
	}

	public String getTarifario() {
		return tarifario;
	}

	public void setTarifarioNombre(String tarifarioNombre) {
		this.tarifarioNombre = tarifarioNombre;
	}

	public String getTarifarioNombre() {
		return tarifarioNombre;
	}

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getProducto() {
		return producto;
	}

	public void setProductoNombre(String productoNombre) {
		this.productoNombre = productoNombre;
	}

	public String getProductoNombre() {
		return productoNombre;
	}

	public void setRecurso(String recurso) {
		this.recurso = recurso;
	}

	public String getRecurso() {
		return recurso;
	}

	public void setValorMinimo(BigDecimal valorMinimo) {
		this.valorMinimo = valorMinimo;
	}

	public BigDecimal getValorMinimo() {
		return valorMinimo;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValorMaximo(BigDecimal valorMaximo) {
		this.valorMaximo = valorMaximo;
	}

	public BigDecimal getValorMaximo() {
		return valorMaximo;
	}

	public void setCantidadMinima(Integer cantidadMinima) {
		this.cantidadMinima = cantidadMinima;
	}

	public Integer getCantidadMinima() {
		return cantidadMinima;
	}

	public void setCantidadMaxima(Integer cantidadMaxima) {
		this.cantidadMaxima = cantidadMaxima;
	}

	public Integer getCantidadMaxima() {
		return cantidadMaxima;
	}

	public void setTotalMinimo(BigDecimal totalMinimo) {
		this.totalMinimo = totalMinimo;
	}

	public BigDecimal getTotalMinimo() {
		return totalMinimo;
	}

	public void setDimension2(String dimension2) {
		this.dimension2 = dimension2;
	}

	public String getDimension2() {
		return dimension2;
	}

	public void setDimension3(String dimension3) {
		this.dimension3 = dimension3;
	}

	public String getDimension3() {
		return dimension3;
	}

	public void setDimension4(String dimension4) {
		this.dimension4 = dimension4;
	}

	public String getDimension4() {
		return dimension4;
	}


	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String createdUser) {
		this.updatedUser = createdUser;
	}

	public String getTarifarioDocumento() {
		return tarifarioDocumento;
	}

	public void setTarifarioDocumento(String tarifarioDocumento) {
		this.tarifarioDocumento = tarifarioDocumento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getProductoDocumento() {
		return productoDocumento;
	}

	public void setProductoDocumento(String productoDocumento) {
		this.productoDocumento = productoDocumento;
	}

}