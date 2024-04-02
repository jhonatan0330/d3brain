package com.softure.mail.infrastructure;

import java.util.List;

// BEGIN region interImport  
import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.domain.MensajeFilterDTO;

@SoftureSqlConnMapper("MensajeMapper")
public interface MensajeMapper extends IBasicMapper<MensajeDTO, MensajeFilterDTO> {

	List<MensajeDTO> mensajesUsuario(MensajeFilterDTO dto);

	List<MensajeDTO> mensajesDisponibles();

	List<MensajeDTO> correosMensaje(@Param("llavePropiedad") String estado, @Param("documento") String documento,
			@Param("modificador") String modificador, @Param("token") String token);

	//List<MensajeDTO> mensajesTransaccion(@Param("transaccion") String transaccion);
}