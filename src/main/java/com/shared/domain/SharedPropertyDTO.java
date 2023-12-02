package com.shared.domain;

import java.util.Date;

public interface SharedPropertyDTO {

	void setPropiedadValor(String propiedadValor);
	String getPropiedadValor();
	void setTipo(String tipo);
	String getTipo();
	void setNombre(String nombre);
	String getNombre();
	void setKey(String key);
	String getKey();
	void setCampo(String campo);
	String getCampo();
	void setValor(String valor);
	String getValor();
	void setTexto(String texto);
	String getTexto();
	void setFechaDefinicion(Date fechaDefinicion);
	Date getFechaDefinicion();
	void setFechaImplementacion(Date fechaImplementacion);
	Date getFechaImplementacion();
	void setCambioCreacion(String cambioCreacion);
	String getCambioCreacion();
	void setCambioEliminacion(String cambioEliminacion);
	String getCambioEliminacion();
	void setRol(String rol);
	String getRol();
	void setRolNombre(String rolNombre);
	String getRolNombre();
	void setRolExcluyente(String rolExcluyente);
	String getRolExcluyente();
	void setRolExcluyenteNombre(String rolExcluyenteNombre);
	String getRolExcluyenteNombre();
	void setFechaInicial(Date fechaInicial);
	Date getFechaInicial();
	void setFechaFinal(Date fechaFinal);
	Date getFechaFinal();
	void setUsuario(String usuario);
	String getUsuario();
	void setUsuarioNombre(String usuarioNombre);
	String getUsuarioNombre();
	void setUsuarioExcluyente(String usuarioExcluyente);
	String getUsuarioExcluyente();
	void setUsuarioExcluyenteNombre(String usuarioExcluyenteNombre);
	String getUsuarioExcluyenteNombre();
	void setMotivo(String motivo);
	String getMotivo() ;
	void setBloqueo(String bloqueo);
	String getBloqueo();
}
