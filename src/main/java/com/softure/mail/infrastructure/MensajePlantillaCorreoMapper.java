package com.softure.mail.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.mail.domain.MensajePlantillaCorreoFilterDTO;

@SoftureSqlConnMapper(value = "MensajePlantillaCorreoMapper")
public interface MensajePlantillaCorreoMapper
		extends IBasicMapper<MensajePlantillaCorreoDTO, MensajePlantillaCorreoFilterDTO> {

	List<MensajePlantillaCorreoDTO> getFullToSynchronize(@Param("process") List<String> process);

}