package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

// BEGIN region interImport  
import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.MensajeDTO;
import com.softure.logisticpymes.domain.filter.MensajeFilterDTO;

public interface MensajeMapper extends IBasicMapper<MensajeDTO, MensajeFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<MensajeDTO> mensajesUsuario(MensajeFilterDTO dto);
	
	List<MensajeDTO> mensajesDisponibles();
	
	List<MensajeDTO> correosMensaje(@Param("llavePropiedad") String estado, @Param("documento") String documento, @Param("modificador") String modificador, @Param("token") String token);
	
	List<MensajeDTO> mensajesTransaccion(@Param("transaccion") String transaccion);
// END region aditionalMethods
}