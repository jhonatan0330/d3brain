package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;
// END region interImport
import com.softure.logisticpymes.dto.OrganizacionDTO;
import com.softure.logisticpymes.dto.filter.OrganizacionFilterDTO;

public interface OrganizacionMapper extends IBasicMapper<OrganizacionDTO, OrganizacionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<OrganizacionDTO> obtenerUsuario(String usuario);
	OrganizacionDTO obtenerPrincipal();
// END region aditionalMethods
}