package com.accounting.plan.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.CuentaContableMovimientoDTO;
import com.accounting.plan.domain.CuentaContableMovimientoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("CuentaContableMovimientoMapper")
public interface CuentaContableMovimientoMapper extends IBasicMapper<CuentaContableMovimientoDTO, CuentaContableMovimientoFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}