package com.softure.logisticpymes.infrastructure.mybatis.mapper;


import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.ConsecutivoDTO;
import com.softure.logisticpymes.domain.filter.ConsecutivoFilterDTO;

public interface ConsecutivoMapper extends IBasicMapper<ConsecutivoDTO, ConsecutivoFilterDTO>{
	

// BEGIN region aditionalMethods  
	String obtenerPrefijo(String documento);
// END region aditionalMethods
}