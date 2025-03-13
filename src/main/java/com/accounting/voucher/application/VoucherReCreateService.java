package com.accounting.voucher.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.api.domain.VoucherPrepareRequest;
import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.shared.domain.SharedToken;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.webservice.application.WebServiceExecuteAPI;

@Service
public class VoucherReCreateService {

	@Autowired @Lazy
	private PedidoVentaSvc pedidoVentaService;
	@Autowired @Lazy
	private PropiedadSvc propertyService;
	@Autowired @Lazy 
	private WebServiceExecuteAPI apiService;
	
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(VoucherPrepareRequest pItem, SharedToken pToken) throws ServerException {
		
		PedidoVentaDTO _document = pedidoVentaService.consultaXId(pItem.getDocumentId());
		return new SharedIdResponse(pItem.getDocumentId(), _document.getNombre(), 
				apiService.prepareApiToExecution(pItem.getServiceId(), _document, null, pToken.getToken(), null));
	}



}
