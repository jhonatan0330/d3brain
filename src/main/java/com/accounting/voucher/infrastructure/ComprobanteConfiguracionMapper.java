package com.accounting.voucher.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.voucher.domain.ComprobanteConfiguracionDTO;
import com.accounting.voucher.domain.ComprobanteConfiguracionFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("ComprobanteConfiguracionMapper")
public interface ComprobanteConfiguracionMapper extends IBasicMapper<ComprobanteConfiguracionDTO, ComprobanteConfiguracionFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}