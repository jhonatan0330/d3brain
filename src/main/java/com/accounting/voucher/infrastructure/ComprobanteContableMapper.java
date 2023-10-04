package com.accounting.voucher.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.voucher.domain.ComprobanteContableDTO;
import com.accounting.voucher.domain.ComprobanteContableFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("ComprobanteContableMapper")
public interface ComprobanteContableMapper extends IBasicMapper<ComprobanteContableDTO, ComprobanteContableFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}