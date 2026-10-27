package d3.process.domain;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import d3.document.domain.PedidoVentaDTO;
import d3.inventory.domain.ProductoDTO;
import d3.shared.domain.BasicNodoDTO;

@Alias("DocumentoPlantillaCaracteristicaDTO")
@JsonInclude(Include.NON_NULL)
public class DocumentoPlantillaCaracteristicaDTO extends BasicNodoDTO {
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

	private List<ProductoDTO> productos;
	private List<PedidoVentaDTO> documentos;

	private String usuarioCreacion;
	private String usuarioEliminacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaEliminacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaCreacion;

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