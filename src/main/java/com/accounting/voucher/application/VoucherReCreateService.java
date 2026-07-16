package com.accounting.voucher.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.api.application.PrepareTypeToCatalogService;
import com.accounting.api.domain.VoucherPrepareRequest;
import com.accounting.plan.domain.TypeDTO;
import com.accounting.voucher.application.base.VoucherService;
import com.accounting.voucher.domain.VoucherFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.shared.domain.SharedIdResponse;
import com.shared.domain.SharedToken;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.webservice.application.WebServiceEjecucionSvc;
import com.softure.webservice.application.WebServiceExecuteAPI;
import com.softure.webservice.domain.WebServiceEjecucionDTO;

@Service
public class VoucherReCreateService {

	private final PedidoVentaSvc pedidoVentaService;
	private final WebServiceExecuteAPI apiService;
	private final VoucherService voucherService;
	private final WebServiceEjecucionSvc webServiceEjecucionSvc;
	private final PrepareTypeToCatalogService typeFindSvc;

	public VoucherReCreateService(@Lazy PedidoVentaSvc pedidoVentaService, @Lazy WebServiceExecuteAPI apiService,
			@Lazy VoucherService voucherService, @Lazy WebServiceEjecucionSvc webServiceEjecucionSvc,
			@Lazy PrepareTypeToCatalogService typeFindSvc) {
		this.pedidoVentaService = pedidoVentaService;
		this.apiService = apiService;
		this.voucherService = voucherService;
		this.webServiceEjecucionSvc = webServiceEjecucionSvc;
		this.typeFindSvc = typeFindSvc;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(VoucherPrepareRequest pItem, SharedToken pToken) throws ServerException {

		TypeDTO type = typeFindSvc.call(pItem.getServiceId(), null, pToken);

		VoucherFilterDTO _filter = new VoucherFilterDTO();
		_filter.setDocument(pItem.getDocumentId());
		_filter.setType(type.getKey());
		_filter.setState(SharedConstants.STATE_ACTIVE);
		if (voucherService.count(_filter) != 0)
			throw new ServerException("Este documento ya tiene un comprobante");

		WebServiceEjecucionDTO _service = webServiceEjecucionSvc.getServiceVoucherActive(pItem.getServiceId(),
				pItem.getDocumentId());
		if (_service != null)
			return new SharedIdResponse(pItem.getDocumentId(), null, null,
					apiService.applyScheduleToExecute(_service, pToken.getToken()));

		PedidoVentaDTO _document = pedidoVentaService.consultaXId(pItem.getDocumentId());
		return new SharedIdResponse(pItem.getDocumentId(), _document.getNombre(), _document.getEstadoNombre(),
				apiService.prepareApiToExecution(pItem.getServiceId(), _document, null, null, pToken.getToken(), null));

	}

}
