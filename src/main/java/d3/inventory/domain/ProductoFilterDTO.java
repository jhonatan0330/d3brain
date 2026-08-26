package d3.inventory.domain;


import org.apache.ibatis.type.Alias;

import d3.java.domain.BasicFilterDTO;

@Alias("ProductoFilterDTO")
public class ProductoFilterDTO extends BasicFilterDTO {

	private String nombre;
	private String codigo;
	private String filtros;
	private String imagen;
	private String categoria;
	private String categoriaNombre;
	private String usuarioRol;
	private Integer cantidadPromocion;
	private Integer cantidadPromocionBase;
	private String documento;
	private String productoBase;
	private String baseNombre;

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

}