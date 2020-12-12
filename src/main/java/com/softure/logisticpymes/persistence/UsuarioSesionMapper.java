package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.UsuarioSesionDTO;
import com.softure.logisticpymes.dto.filter.UsuarioSesionFilterDTO;

public interface UsuarioSesionMapper extends IBasicMapper<UsuarioSesionDTO, UsuarioSesionFilterDTO>{
	

// BEGIN region aditionalMethods  
	int tiempoSesion(String usuario);
// END region aditionalMethods
}