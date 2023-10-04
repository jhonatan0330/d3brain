package com.accounting.voucher.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.voucher.domain.ComprobanteCuentaDetalleDTO;
import com.accounting.voucher.domain.ComprobanteCuentaDetalleFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("ComprobanteCuentaDetalleMapper")
public interface ComprobanteCuentaDetalleMapper extends IBasicMapper<ComprobanteCuentaDetalleDTO, ComprobanteCuentaDetalleFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}