package com.accounting.plan.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.CuentaContableDTO;
import com.accounting.plan.domain.CuentaContableFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("CuentaContableMapper")
public interface CuentaContableMapper extends IBasicMapper<CuentaContableDTO, CuentaContableFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}