package com.softure.authorization.infrastructure;

import java.util.List;

import com.softure.authorization.domain.ModuloContratadoDTO;
import com.softure.authorization.domain.ModuloContratadoFilterDTO;
import com.softure.java.domain.IBasicMapper;

public interface ModuloContratadoMapper extends IBasicMapper<ModuloContratadoDTO, ModuloContratadoFilterDTO>{
	

// BEGIN region aditionalMethods  
	 List<ModuloContratadoDTO> modulosUsuario(ModuloContratadoFilterDTO dto);
// END region aditionalMethods
}