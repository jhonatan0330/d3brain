package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

// BEGIN region interImport  
import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.UsuarioDTO;
import com.softure.logisticpymes.domain.filter.UsuarioFilterDTO;

public interface UsuarioMapper extends IBasicMapper<UsuarioDTO, UsuarioFilterDTO>{
	

	List<UsuarioDTO> listarRol(UsuarioFilterDTO dto);

// BEGIN region aditionalMethods  
	List<UsuarioDTO> getUsersState(@Param("state")String state, @Param("token")String token);
// END region aditionalMethods
}