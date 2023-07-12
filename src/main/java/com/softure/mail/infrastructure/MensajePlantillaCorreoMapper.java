package com.softure.mail.infrastructure;


import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.mail.domain.MensajePlantillaCorreoFilterDTO;

public interface MensajePlantillaCorreoMapper extends IBasicMapper<MensajePlantillaCorreoDTO, MensajePlantillaCorreoFilterDTO>{

	List<MensajePlantillaCorreoDTO> getFullToSynchronize();
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}