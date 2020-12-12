package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;
// END region interImport
import com.softure.logisticpymes.dto.OrganizacionDTO;
import com.softure.logisticpymes.dto.filter.OrganizacionFilterDTO;

public interface OrganizacionMapper extends IBasicMapper<OrganizacionDTO, OrganizacionFilterDTO>{
	

	OrganizacionDTO obtenerPrincipal(OrganizacionFilterDTO dto);

// BEGIN region aditionalMethods  
	List<OrganizacionDTO> obtenerUsuario(String usuario);
// END region aditionalMethods
}