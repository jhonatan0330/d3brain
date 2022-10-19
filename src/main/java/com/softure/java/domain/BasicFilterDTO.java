package com.softure.java.domain;

public abstract class BasicFilterDTO {

	private Integer paginacionRegistroInicial;
	private Integer paginacionRegistroFinal;
	private String filtroParametro;
	private String llaveTabla;
	private String estado;
	private String securityToken;
	
	public Integer getPaginacionRegistroInicial() {
		return paginacionRegistroInicial;
	}


	public void setPaginacionRegistroInicial(Integer paginacionRegistroInicial) {
		this.paginacionRegistroInicial = paginacionRegistroInicial;
	}


	public Integer getPaginacionRegistroFinal() {
		return paginacionRegistroFinal;
	}


	public void setPaginacionRegistroFinal(Integer paginacionRegistroFinal) {
		this.paginacionRegistroFinal = paginacionRegistroFinal;
	}
	
	public String getLlaveTabla() {
		return llaveTabla;
	}

	public void setLlaveTabla(String llaveTabla) {
		this.llaveTabla = llaveTabla;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstado() {
		return estado;
	}


	public void setFiltroParametro(String filtroParametro) {
		this.filtroParametro = filtroParametro;
	}


	public String getFiltroParametro() {
		return filtroParametro;
	}


	public void setSecurityToken(String securityToken) {
		this.securityToken = securityToken;
	}


	public String getSecurityToken() {
		return securityToken;
	}
	

}
