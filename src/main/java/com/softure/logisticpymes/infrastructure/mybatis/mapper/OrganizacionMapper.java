package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.OrganizacionDTO;
import com.softure.logisticpymes.domain.filter.OrganizacionFilterDTO;

public interface OrganizacionMapper extends IBasicMapper<OrganizacionDTO, OrganizacionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<OrganizacionDTO> obtenerUsuario(String usuario);
	OrganizacionDTO obtenerPrincipal();
// END region aditionalMethods
}