package com.softure.logisticpymes.infrastructure.mybatis.mapper;


import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.CuentaDTO;
import com.softure.logisticpymes.domain.filter.CuentaFilterDTO;

public interface CuentaMapper extends IBasicMapper<CuentaDTO, CuentaFilterDTO>{
	

// BEGIN region aditionalMethods  
	Long sobregiro(String documento);
// END region aditionalMethods
}