package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.UsuarioOrganizacionDTO;
import com.softure.logisticpymes.domain.filter.UsuarioOrganizacionFilterDTO;

public interface UsuarioOrganizacionMapper extends IBasicMapper<UsuarioOrganizacionDTO, UsuarioOrganizacionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<UsuarioOrganizacionDTO> sincronizarUsuarios();
// END region aditionalMethods
}