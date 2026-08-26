package d3.document_transition.domain;

import java.util.Date;

import org.apache.ibatis.type.Alias;

import com.fasterxml.jackson.annotation.JsonFormat;
import d3.java.domain.BasicDTO;

@Alias("DocumentoRelacionGestorDTO")
public class DocumentoRelacionGestorDTO extends BasicDTO {

	private String documentoPrincipal;
	private String documentoModificador;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fecha;
	private String estadoInicial;
	private String estadoFinal;
	private String usuario;
	private String responsable;
	private String responsableImagen;
	private String modificadorNombre;
	private String comentario;
	private String plantilla;
	private String plantillaNombre;
	private String valores;
	private String transaccion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date cierre;
	private String nombre;
	private boolean estadorepetido;
	private String adjunto;

	public void setDocumentoPrincipal(String documentoPrincipal) {
		this.documentoPrincipal = documentoPrincipal;
	}

	public String getDocumentoPrincipal() {
		return documentoPrincipal;
	}

	public void setDocumentoModificador(String documentoModificador) {
		this.documentoModificador = documentoModificador;
	}

	public String getDocumentoModificador() {
		return documentoModificador;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setEstadoInicial(String estadoInicial) {
		this.estadoInicial = estadoInicial;
	}

	public String getEstadoInicial() {
		return estadoInicial;
	}

	public void setEstadoFinal(String estadoFinal) {
		this.estadoFinal = estadoFinal;
	}

	public String getEstadoFinal() {
		return estadoFinal;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	public String getResponsable() {
		return responsable;
	}

	public void setResponsableImagen(String responsableImagen) {
		this.responsableImagen = responsableImagen;
	}

	public String getResponsableImagen() {
		return responsableImagen;
	}

	public void setModificadorNombre(String modificadorNombre) {
		this.modificadorNombre = modificadorNombre;
	}

	public String getModificadorNombre() {
		return modificadorNombre;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getComentario() {
		return comentario;
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

	public void setValores(String valores) {
		this.valores = valores;
	}

	public String getValores() {
		return valores;
	}

	public void setTransaccion(String transaccion) {
		this.transaccion = transaccion;
	}

	public String getTransaccion() {
		return transaccion;
	}

	public void setCierre(Date cierre) {
		this.cierre = cierre;
	}

	public Date getCierre() {
		return cierre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNombre() {
		return nombre;
	}

	public void setAdjunto(String adjunto) {
		this.adjunto = adjunto;
	}

	public String getAdjunto() {
		return adjunto;
	}

	public boolean getEstadorepetido() {
		return estadorepetido;
	}

	public void setEstadorepetido(boolean estadorepetido) {
		this.estadorepetido = estadorepetido;
	}

}