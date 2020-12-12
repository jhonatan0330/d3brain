package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.ConsecutivoDTO;
import com.softure.logisticpymes.dto.filter.ConsecutivoFilterDTO;

public interface ConsecutivoMapper extends IBasicMapper<ConsecutivoDTO, ConsecutivoFilterDTO>{
	

// BEGIN region aditionalMethods  
	String obtenerPrefijo(String documento);
// END region aditionalMethods
}