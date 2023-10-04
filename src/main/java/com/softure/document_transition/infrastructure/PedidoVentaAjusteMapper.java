package com.softure.document_transition.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.document_transition.domain.PedidoVentaAjusteDTO;
import com.softure.document_transition.domain.PedidoVentaAjusteFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper("PedidoVentaAjusteMapper")
public interface PedidoVentaAjusteMapper extends IBasicMapper<PedidoVentaAjusteDTO, PedidoVentaAjusteFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}