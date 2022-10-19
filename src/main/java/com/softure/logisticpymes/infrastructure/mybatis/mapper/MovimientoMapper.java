package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.MovimientoDTO;
import com.softure.logisticpymes.domain.filter.MovimientoFilterDTO;

public interface MovimientoMapper extends IBasicMapper<MovimientoDTO, MovimientoFilterDTO>{
	

	List<MovimientoDTO> obtenerMovimientoAnteriorFecha(MovimientoFilterDTO dto);

	List<MovimientoDTO> obtenerMovimientoSiguienteFecha(MovimientoFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}