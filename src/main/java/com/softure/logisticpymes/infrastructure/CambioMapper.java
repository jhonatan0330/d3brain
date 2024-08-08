package com.softure.logisticpymes.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.CambioDTO;
import com.softure.logisticpymes.domain.CambioFilterDTO;

@SoftureSqlConnMapper(value = "CambioMapper")
public interface CambioMapper extends IBasicMapper<CambioDTO, CambioFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}