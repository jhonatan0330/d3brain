package com.softure.process_form.infrastructure;


import com.softure.java.domain.IBasicMapper;
import com.softure.process_form.domain.ConsecutivoDTO;
import com.softure.process_form.domain.ConsecutivoFilterDTO;

public interface ConsecutivoMapper extends IBasicMapper<ConsecutivoDTO, ConsecutivoFilterDTO>{
	

// BEGIN region aditionalMethods  
	String obtenerPrefijo(String documento);
// END region aditionalMethods
}