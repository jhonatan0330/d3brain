package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
// END region interImport
import com.softure.logisticpymes.dto.CuentaDTO;
import com.softure.logisticpymes.dto.filter.CuentaFilterDTO;

public interface CuentaMapper extends IBasicMapper<CuentaDTO, CuentaFilterDTO>{
	

// BEGIN region aditionalMethods  
	Long sobregiro(String documento);
// END region aditionalMethods
}