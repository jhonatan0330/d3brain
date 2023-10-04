package com.softure.tariff.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;

@SoftureSqlConnMapper("TarifarioMapper")
public interface TarifarioMapper extends IBasicMapper<TarifarioDTO, TarifarioFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}