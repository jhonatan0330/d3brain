package com.softure.logisticpymes.infrastructure.mybatis.mapper;

import java.util.List;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.PostRespuestaDTO;
import com.softure.logisticpymes.domain.filter.PostRespuestaFilterDTO;

public interface PostRespuestaMapper extends IBasicMapper<PostRespuestaDTO, PostRespuestaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PostRespuestaDTO> listarEnOrden(PostRespuestaFilterDTO dto);
// END region aditionalMethods
}