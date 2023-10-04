package com.accounting.plan.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.CuentaAuxiliarPlantillaDTO;
import com.accounting.plan.domain.CuentaAuxiliarPlantillaFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("CuentaAuxiliarPlantillaMapper")
public interface CuentaAuxiliarPlantillaMapper extends IBasicMapper<CuentaAuxiliarPlantillaDTO, CuentaAuxiliarPlantillaFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}