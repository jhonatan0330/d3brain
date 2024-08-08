package com.softure.process_form.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.process_form.domain.ConsecutivoDTO;
import com.softure.process_form.domain.ConsecutivoFilterDTO;

@SoftureSqlConnMapper(value = "ConsecutivoMapper")
public interface ConsecutivoMapper extends IBasicMapper<ConsecutivoDTO, ConsecutivoFilterDTO>{
	

// BEGIN region aditionalMethods  
	String obtenerPrefijo(String documento);
// END region aditionalMethods
}