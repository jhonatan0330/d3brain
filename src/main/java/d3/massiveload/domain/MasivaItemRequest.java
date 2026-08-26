package d3.massiveload.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import d3.shared.domain.SharedDataObject;

public class MasivaItemRequest extends SharedDataObject {

	private String carga;
	private String documento;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaSerializacion;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd@HH:mm:ss.SSSZ", timezone = "America/Bogota")
	private Date fechaSincronizacion;
	private String modelo;
	private String nombre;
	private String progreso;
	private Date createdAt;
	private String createdUser;
	private Date updatedAt;
	private String updatedUser;
	@JsonProperty(access = Access.READ_ONLY)
	private String state;

	public String getCarga() {
		return carga;
	}

	public void setCarga(String carga) {
		this.carga = carga;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Date getFechaSerializacion() {
		return fechaSerializacion;
	}

	public void setFechaSerializacion(Date fechaSerializacion) {
		this.fechaSerializacion = fechaSerializacion;
	}

	public Date getFechaSincronizacion() {
		return fechaSincronizacion;
	}

	public void setFechaSincronizacion(Date fechaSincronizacion) {
		this.fechaSincronizacion = fechaSincronizacion;
	}

	public String getModelo() {
		return modelo;
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getProgreso() {
		return progreso;
	}

	public void setProgreso(String progreso) {
		this.progreso = progreso;
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

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public MassiveItemDTO toModel() {
		var cargaMasivaItemModel = new MassiveItemDTO();
		cargaMasivaItemModel.setKey(this.getKey());
		cargaMasivaItemModel.setCarga(this.carga);
		cargaMasivaItemModel.setModelo(this.modelo);
		cargaMasivaItemModel.setProgreso(this.progreso);
		cargaMasivaItemModel.setFechaSerializacion(this.fechaSerializacion);
		cargaMasivaItemModel.setFechaSincronizacion(this.fechaSincronizacion);
		cargaMasivaItemModel.setDocumento(this.documento);
		cargaMasivaItemModel.setNombre(this.nombre);
		return cargaMasivaItemModel;
	}
}
