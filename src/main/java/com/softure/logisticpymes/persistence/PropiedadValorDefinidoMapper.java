package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.filter.PropiedadValorDefinidoFilterDTO;

public interface PropiedadValorDefinidoMapper extends IBasicMapper<PropiedadValorDefinidoDTO, PropiedadValorDefinidoFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PropiedadValorDefinidoDTO> listarPorOrigen(PropiedadValorDefinidoFilterDTO dto);
// END region aditionalMethods
}