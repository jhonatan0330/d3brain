package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.ModuloContratadoDTO;
import com.softure.logisticpymes.domain.filter.ModuloContratadoFilterDTO;

public interface ModuloContratadoMapper extends IBasicMapper<ModuloContratadoDTO, ModuloContratadoFilterDTO>{
	

// BEGIN region aditionalMethods  
	 List<ModuloContratadoDTO> modulosUsuario(ModuloContratadoFilterDTO dto);
// END region aditionalMethods
}