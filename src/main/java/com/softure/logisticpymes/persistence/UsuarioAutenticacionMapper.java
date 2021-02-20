package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.UsuarioAutenticacionDTO;
import com.softure.logisticpymes.dto.filter.UsuarioAutenticacionFilterDTO;

public interface UsuarioAutenticacionMapper extends IBasicMapper<UsuarioAutenticacionDTO, UsuarioAutenticacionFilterDTO>{
	

// BEGIN region aditionalMethods  
	String consultarValidez();
	String versionActual();
	String fechaMinima();
	
	int cantidadAsignaciones(String usuario);

// END region aditionalMethods
}