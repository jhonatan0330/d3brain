package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.RolAccesoDTO;
import com.softure.logisticpymes.domain.filter.RolAccesoFilterDTO;

public interface RolAccesoMapper extends IBasicMapper<RolAccesoDTO, RolAccesoFilterDTO>{
	

	List<RolAccesoDTO> consultaUsuarioDocumento(RolAccesoFilterDTO dto);

// BEGIN region aditionalMethods  
	int permisosCompletos(String user);
// END region aditionalMethods
}