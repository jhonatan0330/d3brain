package com.accounting.plan.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.CuentaAuxiliarDocumentoDTO;
import com.accounting.plan.domain.CuentaAuxiliarDocumentoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("CuentaAuxiliarDocumentoMapper")
public interface CuentaAuxiliarDocumentoMapper extends IBasicMapper<CuentaAuxiliarDocumentoDTO, CuentaAuxiliarDocumentoFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}