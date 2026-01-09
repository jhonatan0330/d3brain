package com.softure.process_form.domain;

import java.util.Date;
import java.util.List;


import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.logisticpymes.domain.BasicParamDTO;

@Alias("DocumentoPlantillaCaracteristicaDTO")
@JsonInclude(Include.NON_NULL)
public class DocumentoPlantillaCaracteristicaDTO extends BasicParamDTO
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
	public static final String GPS_MAP = "M";
	public static final String SECCION = "S";
	public static final String VINCULO = "C";
	public static final String INFORMATIVO = "V";

	private String objetivo;
	private String plantilla;
	private String plantillaNombre;
	private String formato;
	private String nombre;
	private String codigo;
	private Integer orden;
	private String imagen;
	private List<ProductoDTO> productos;
	private List<PedidoVentaDTO> documentos;
	
	private String usuarioCreacion;
	private String usuarioEliminacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEliminacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaCreacion;

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

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public String getUsuarioEliminacion() {
		return usuarioEliminacion;
	}

	public void setUsuarioEliminacion(String usuarioEliminacion) {
		this.usuarioEliminacion = usuarioEliminacion;
	}

	public Date getFechaEliminacion() {
		return fechaEliminacion;
	}

	public void setFechaEliminacion(Date fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

}