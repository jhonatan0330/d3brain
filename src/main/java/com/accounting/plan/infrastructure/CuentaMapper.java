package com.accounting.plan.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.CuentaDTO;
import com.accounting.plan.domain.CuentaFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("CuentaAccountingMapper")
public interface CuentaMapper extends IBasicMapper<CuentaDTO, CuentaFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}