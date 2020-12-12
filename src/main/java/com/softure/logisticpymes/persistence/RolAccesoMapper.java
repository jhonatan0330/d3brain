package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.RolAccesoDTO;
import com.softure.logisticpymes.dto.filter.RolAccesoFilterDTO;

public interface RolAccesoMapper extends IBasicMapper<RolAccesoDTO, RolAccesoFilterDTO>{
	

	List<RolAccesoDTO> consultaUsuarioDocumento(RolAccesoFilterDTO dto);

// BEGIN region aditionalMethods  
	int permisosCompletos(String user);
// END region aditionalMethods
}