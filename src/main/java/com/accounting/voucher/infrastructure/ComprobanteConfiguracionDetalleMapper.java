package com.accounting.voucher.infrastructure;


import com.accounting.AccountingConnMapper;
import com.accounting.voucher.domain.ComprobanteConfiguracionDetalleDTO;
import com.accounting.voucher.domain.ComprobanteConfiguracionDetalleFilterDTO;
import com.softure.java.domain.IBasicMapper;

@AccountingConnMapper("ComprobanteConfiguracionDetalleMapper")
public interface ComprobanteConfiguracionDetalleMapper extends IBasicMapper<ComprobanteConfiguracionDetalleDTO, ComprobanteConfiguracionDetalleFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}