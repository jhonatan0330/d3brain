package com.softure.authentication.domain;


import java.util.List;

import org.apache.ibatis.type.Alias;

import com.softure.logisticpymes.domain.BasicParamDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Alias("OrganizacionDTO")
public class OrganizacionDTO extends BasicParamDTO
{
	private String nombre;
	private String principal;
	private String servidor;
	private String usuarioSystem;
	private String imagen;
	private String slogan;
	private String mensajeIngreso;
	private String codigo;
	private List<DocumentoPlantillaDTO> templates;
	private String publicToken;

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setPrincipal(String principal) {
		this.principal = principal;
	}
	
	public String getPrincipal() {
		return principal;
	}
	public void setServidor(String servidor) {
		this.servidor = servidor;
	}
	
	public String getServidor() {
		return servidor;
	}
	public void setUsuarioSystem(String usuarioSystem) {
		this.usuarioSystem = usuarioSystem;
	}
	
	public String getUsuarioSystem() {
		return usuarioSystem;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	
	public String getImagen() {
		return imagen;
	}
	public void setSlogan(String slogan) {
		this.slogan = slogan;
	}
	
	public String getSlogan() {
		return slogan;
	}

	public void setMensajeIngreso(String mensajeIngreso) {
		this.mensajeIngreso = mensajeIngreso;
	}
	
	public String getMensajeIngreso() {
		return mensajeIngreso;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public List<DocumentoPlantillaDTO> getTemplates() {
		return templates;
	}

	public void setTemplates(List<DocumentoPlantillaDTO> templates) {
		this.templates = templates;
	}

	public String getPublicToken() {
		return publicToken;
	}

	public void setPublicToken(String publicToken) {
		this.publicToken = publicToken;
	}

}