package com.softure.money.infrastructure;

import java.util.List;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.money.domain.MovimientoDTO;
import com.softure.money.domain.MovimientoFilterDTO;

@SoftureSqlConnMapper(value = "MovimientoMapper")
public interface MovimientoMapper extends IBasicMapper<MovimientoDTO, MovimientoFilterDTO> {

	List<MovimientoDTO> obtenerMovimientoAnteriorFecha(MovimientoFilterDTO dto);

	List<MovimientoDTO> obtenerMovimientoSiguienteFecha(MovimientoFilterDTO dto);

}