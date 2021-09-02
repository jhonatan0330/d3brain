package com.softure.logisticpymes.persistence;

import java.util.List;

// BEGIN region interImport  
import org.apache.ibatis.annotations.Param;
// END region interImport
import com.softure.logisticpymes.dto.MensajeDTO;
import com.softure.logisticpymes.dto.filter.MensajeFilterDTO;

public interface MensajeMapper extends IBasicMapper<MensajeDTO, MensajeFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<MensajeDTO> mensajesUsuario(MensajeFilterDTO dto);
	
	List<MensajeDTO> mensajesDisponibles();
	
	List<MensajeDTO> correosMensaje(@Param("llavePropiedad") String estado, @Param("documento") String documento);
// END region aditionalMethods
}