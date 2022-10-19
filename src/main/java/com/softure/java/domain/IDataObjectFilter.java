package com.softure.java.domain;

public interface IDataObjectFilter {

	String getLlaveTabla() ;

	void setLlaveTabla(String llaveTabla);

	String getEstado();
	
	void setEstado(String estado);

	int getPaginacionRegistroInicial();

	void setPaginacionRegistroInicial(int paginacionRegistroInicial) ;

	int getPaginacionRegistroFinal();

	void setPaginacionRegistroFinal(int paginacionRegistroFinal);

	void setFiltroParametro(String filtroParametro);

	String getFiltroParametro() ;

	void setSecurityToken(String securityToken) ;

	String getSecurityToken();
}
