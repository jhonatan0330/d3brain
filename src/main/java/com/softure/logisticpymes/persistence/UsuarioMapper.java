package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
import org.apache.ibatis.annotations.Param;
// END region interImport
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.logisticpymes.dto.filter.UsuarioFilterDTO;

public interface UsuarioMapper extends IBasicMapper<UsuarioDTO, UsuarioFilterDTO>{
	

	List<UsuarioDTO> listarRol(UsuarioFilterDTO dto);

// BEGIN region aditionalMethods  
	List<UsuarioDTO> getUsersState(@Param("state")String state, @Param("token")String token);
// END region aditionalMethods
}