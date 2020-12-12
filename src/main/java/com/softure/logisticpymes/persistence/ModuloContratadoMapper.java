package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.ModuloContratadoDTO;
import com.softure.logisticpymes.dto.filter.ModuloContratadoFilterDTO;

public interface ModuloContratadoMapper extends IBasicMapper<ModuloContratadoDTO, ModuloContratadoFilterDTO>{
	

// BEGIN region aditionalMethods  
	 List<ModuloContratadoDTO> modulosUsuario(ModuloContratadoFilterDTO dto);
// END region aditionalMethods
}