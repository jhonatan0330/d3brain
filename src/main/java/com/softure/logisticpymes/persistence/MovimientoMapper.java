package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.MovimientoDTO;
import com.softure.logisticpymes.dto.filter.MovimientoFilterDTO;

public interface MovimientoMapper extends IBasicMapper<MovimientoDTO, MovimientoFilterDTO>{
	

	List<MovimientoDTO> obtenerMovimientoAnteriorFecha(MovimientoFilterDTO dto);

	List<MovimientoDTO> obtenerMovimientoSiguienteFecha(MovimientoFilterDTO dto);

// BEGIN region aditionalMethods  
// END region aditionalMethods
}