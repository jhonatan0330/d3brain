package com.softure.property.infrastructure;

import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.PropiedadValorDefinidoFilterDTO;

@SoftureSqlConnMapper(value = "PropiedadValorDefinidoMapper")
public interface PropiedadValorDefinidoMapper
		extends IBasicMapper<PropiedadValorDefinidoDTO, PropiedadValorDefinidoFilterDTO> {

	List<PropiedadValorDefinidoDTO> listarPorOrigen(PropiedadValorDefinidoFilterDTO dto);

	List<PropiedadValorDefinidoDTO> getFullToSynchronize();
}