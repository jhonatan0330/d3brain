package com.softure.property.infrastructure;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.PropiedadValorDefinidoFilterDTO;

public interface PropiedadValorDefinidoMapper extends IBasicMapper<PropiedadValorDefinidoDTO, PropiedadValorDefinidoFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PropiedadValorDefinidoDTO> listarPorOrigen(PropiedadValorDefinidoFilterDTO dto);
// END region aditionalMethods

	List<PropiedadValorDefinidoDTO> getFullToSynchronize();
}