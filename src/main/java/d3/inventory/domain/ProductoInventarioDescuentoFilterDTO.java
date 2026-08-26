package d3.inventory.domain;


import org.apache.ibatis.type.Alias;

import d3.java.domain.BasicFilterDTO;

@Alias("ProductoInventarioDescuentoFilterDTO")
public class ProductoInventarioDescuentoFilterDTO extends BasicFilterDTO {

	private String producto;
	private String productoDescontar;
	private String productoDescontarNombre;
	private String caracteristica;
	private String caracteristicaNombre;
	private String documento;

	public void setProducto(String producto) {
		this.producto = producto;
	}

	public String getProducto() {
		return producto;
	}

	public void setProductoDescontar(String productoDescontar) {
		this.productoDescontar = productoDescontar;
	}

	public String getProductoDescontar() {
		return productoDescontar;
	}

	public void setProductoDescontarNombre(String productoDescontarNombre) {
		this.productoDescontarNombre = productoDescontarNombre;
	}

	public String getProductoDescontarNombre() {
		return productoDescontarNombre;
	}

	public void setCaracteristica(String caracteristica) {
		this.caracteristica = caracteristica;
	}

	public String getCaracteristica() {
		return caracteristica;
	}

	public void setCaracteristicaNombre(String caracteristicaNombre) {
		this.caracteristicaNombre = caracteristicaNombre;
	}

	public String getCaracteristicaNombre() {
		return caracteristicaNombre;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

}