package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.UsuarioOrganizacionDTO;
import com.softure.logisticpymes.dto.filter.UsuarioOrganizacionFilterDTO;

public interface UsuarioOrganizacionMapper extends IBasicMapper<UsuarioOrganizacionDTO, UsuarioOrganizacionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<UsuarioOrganizacionDTO> sincronizarUsuarios();
// END region aditionalMethods
}