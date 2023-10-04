package com.accounting.plan.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.CatalogoContableDTO;
import com.accounting.plan.domain.CatalogoContableFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("CatalogoContableMapper")
public interface CatalogoContableMapper extends IBasicMapper<CatalogoContableDTO, CatalogoContableFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}