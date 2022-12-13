package com.softure.authentication.infrastructure;


// BEGIN region interImport  
import java.util.List;

import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.OrganizacionFilterDTO;
import com.softure.java.domain.IBasicMapper;

public interface OrganizacionMapper extends IBasicMapper<OrganizacionDTO, OrganizacionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<OrganizacionDTO> obtenerUsuario(String usuario);
	OrganizacionDTO obtenerPrincipal();
// END region aditionalMethods
}