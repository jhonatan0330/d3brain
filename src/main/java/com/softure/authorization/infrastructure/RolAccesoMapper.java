package com.softure.authorization.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper("RolAccesoMapper")
public interface RolAccesoMapper extends IBasicMapper<RolAccesoDTO, RolAccesoFilterDTO>{
	

	List<RolAccesoDTO> consultaUsuarioDocumento(RolAccesoFilterDTO dto);

// BEGIN region aditionalMethods  
	int permisosCompletos(String user);
	List<RolAccesoDTO> getFullToSynchronize(@Param("process") List<String> process);
// END region aditionalMethods

}