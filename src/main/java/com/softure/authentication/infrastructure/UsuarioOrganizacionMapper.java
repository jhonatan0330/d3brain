package com.softure.authentication.infrastructure;

import java.util.List;

import com.softure.authentication.domain.UsuarioOrganizacionDTO;
import com.softure.authentication.domain.UsuarioOrganizacionFilterDTO;
import com.softure.java.domain.IBasicMapper;

public interface UsuarioOrganizacionMapper extends IBasicMapper<UsuarioOrganizacionDTO, UsuarioOrganizacionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<UsuarioOrganizacionDTO> sincronizarUsuarios();
// END region aditionalMethods
}