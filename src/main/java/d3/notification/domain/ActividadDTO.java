package d3.notification.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.java.domain.BasicDTO;

import org.apache.ibatis.type.Alias;

@Alias("ActividadDTO")
public class ActividadDTO extends BasicDTO
{

	private String responsable;
	private String responsableIdentificacion;
	private String responsableNombre;
	private String documento;
	private PedidoVentaDTO documentoDTO;
	private String responsableFoto;
	private String comentario;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaArrancar;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaRegistro;
	private String usuarioRegistro;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaInactivo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaTerminar;
	private String usuarioInactivo;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaLeido;

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsableIdentificacion(String responsableIdentificacion) {
		this.responsableIdentificacion = responsableIdentificacion;
	}

	public String getResponsableIdentificacion() {
		return responsableIdentificacion;
	}

	public void setResponsableNombre(String responsableNombre) {
		this.responsableNombre = responsableNombre;
	}

	public String getResponsableNombre() {
		return responsableNombre;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumentoDTO(PedidoVentaDTO documentoDTO) {
		this.documentoDTO = documentoDTO;
	}

	public PedidoVentaDTO getDocumentoDTO() {
		return documentoDTO;
	}

	public void setResponsableFoto(String responsableFoto) {
		this.responsableFoto = responsableFoto;
	}

	public String getResponsableFoto() {
		return responsableFoto;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getComentario() {
		return comentario;
	}

	public void setFechaArrancar(Date fechaArrancar) {
		this.fechaArrancar = fechaArrancar;
	}

	public Date getFechaArrancar() {
		return fechaArrancar;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setUsuarioRegistro(String usuarioRegistro) {
		this.usuarioRegistro = usuarioRegistro;
	}

	public String getUsuarioRegistro() {
		return usuarioRegistro;
	}

	public void setFechaInactivo(Date fechaInactivo) {
		this.fechaInactivo = fechaInactivo;
	}

	public Date getFechaInactivo() {
		return fechaInactivo;
	}

	public void setFechaTerminar(Date fechaTerminar) {
		this.fechaTerminar = fechaTerminar;
	}

	public Date getFechaTerminar() {
		return fechaTerminar;
	}

	public void setUsuarioInactivo(String usuarioInactivo) {
		this.usuarioInactivo = usuarioInactivo;
	}

	public String getUsuarioInactivo() {
		return usuarioInactivo;
	}

	public void setFechaLeido(Date fechaLeido) {
		this.fechaLeido = fechaLeido;
	}

	public Date getFechaLeido() {
		return fechaLeido;
	}

}