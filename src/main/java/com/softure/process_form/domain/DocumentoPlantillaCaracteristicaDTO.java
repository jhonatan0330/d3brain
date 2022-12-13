package com.softure.process_form.domain;

import java.util.List;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.domain.CategoriaProductoDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.logisticpymes.domain.BasicParamDTO;

@Alias("DocumentoPlantillaCaracteristicaDTO")
public class DocumentoPlantillaCaracteristicaDTO extends BasicParamDTO
// BEGIN region interfaces  
// END region interfaces
{
	public static final String TEXTO = "T";
	public static final String FECHA = "F";
	public static final String PROCESO = "Z";
	public static final String NUMERO = "N";
	public static final String BINARIO = "I";
	public static final String PRODUCTO = "J";
	public static final String ARCHIVO = "A";
	public static final String CROQUIS = "B";
	public static final String CONFIGURACION = "G";
	public static final String DISPONIBILIDAD = "U";
	public static final String PRODUCTO_LISTA = "Q";
	public static final String GPS = "P";
	public static final String SECCION = "S";

	private String objetivo;
	private String plantilla;
	private String plantillaNombre;
	private String formato;
	private String nombre;
	private String codigo;
	private Integer orden;
	private String imagen;
	private List<CategoriaProductoDTO> categorias;
	private List<ProductoDTO> productos;
	private List<PedidoVentaDTO> documentos;

	public void setObjetivo(String objetivo) {
		this.objetivo = objetivo;
	}
	
	public String getObjetivo() {
		return objetivo;
	}
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
	public void setCategorias(List<CategoriaProductoDTO> categorias) {
		this.categorias = categorias;
	}
	
	public List<CategoriaProductoDTO> getCategorias() {
		return categorias;
	}
	public void setProductos(List<ProductoDTO> productos) {
		this.productos = productos;
	}
	
	public List<ProductoDTO> getProductos() {
		return productos;
	}
	public void setDocumentos(List<PedidoVentaDTO> documentos) {
		this.documentos = documentos;
	}
	
	public List<PedidoVentaDTO> getDocumentos() {
		return documentos;
	}
// BEGIN region metodoInterfaces
// END region metodoInterfaces

}