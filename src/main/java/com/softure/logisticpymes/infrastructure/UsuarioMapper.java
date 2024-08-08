package com.softure.logisticpymes.infrastructure;

import java.util.List;

// BEGIN region interImport  
import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;

@SoftureSqlConnMapper(value = "UsuarioMapper")
public interface UsuarioMapper extends IBasicMapper<UsuarioDTO, UsuarioFilterDTO>{
	

	List<UsuarioDTO> listarRol(UsuarioFilterDTO dto);

// BEGIN region aditionalMethods  
	List<UsuarioDTO> getUsersState(@Param("document")String document);
// END region aditionalMethods
}