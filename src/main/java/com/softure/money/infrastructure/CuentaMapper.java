package com.softure.money.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.money.domain.CuentaDTO;
import com.softure.money.domain.CuentaFilterDTO;

@SoftureSqlConnMapper("CuentaMapper")
public interface CuentaMapper extends IBasicMapper<CuentaDTO, CuentaFilterDTO>{
	

// BEGIN region aditionalMethods  
	Long sobregiro(String documento);
// END region aditionalMethods
}