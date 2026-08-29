package d3.accounting.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.accounting.application.base.VoucherService;
import d3.accounting.domain.TypeDTO;
import d3.accounting.domain.VoucherFilterDTO;
import d3.accounting.domain.VoucherPrepareRequest;
import d3.document.application.PedidoVentaSvc;
import d3.document.domain.PedidoVentaDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.shared.domain.SharedIdResponse;
import d3.shared.domain.SharedToken;
import d3.webservice.application.WebServiceEjecucionSvc;
import d3.webservice.application.WebServiceExecuteAPI;
import d3.webservice.domain.WebServiceEjecucionDTO;

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
