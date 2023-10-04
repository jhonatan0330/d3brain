package com.accounting.plan.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.plan.domain.CatalogoDTO;
import com.accounting.plan.domain.CatalogoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("CatalogoAccountingMapper")
public interface CatalogoMapper extends IBasicMapper<CatalogoDTO, CatalogoFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}