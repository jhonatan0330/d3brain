package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.domain.filter.PropiedadValorDefinidoFilterDTO;

public interface PropiedadValorDefinidoMapper extends IBasicMapper<PropiedadValorDefinidoDTO, PropiedadValorDefinidoFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PropiedadValorDefinidoDTO> listarPorOrigen(PropiedadValorDefinidoFilterDTO dto);
// END region aditionalMethods
}