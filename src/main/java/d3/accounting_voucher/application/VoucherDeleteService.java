package d3.accounting_voucher.application;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.accounting_plan.application.base.StackVoucherService;
import d3.accounting_plan.domain.StackVoucherDTO;
import d3.accounting_plan.domain.StackVoucherFilterDTO;
import d3.accounting_voucher.application.base.VoucherService;
import d3.accounting_voucher.domain.VoucherDTO;
import d3.accounting_voucher.domain.VoucherFilterDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.shared.domain.SharedIdResponse;
import d3.document_execution.application.field.Propiedades;
import d3.property.application.PropertyGetWithCacheService;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import d3.webservice.application.WebServiceEjecucionSvc;
import d3.webservice.domain.WebServiceEjecucionDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class VoucherDeleteService {

	private final VoucherService voucherService;
	private final StackVoucherService stackBasicService;
	private final WebServiceEjecucionSvc taskService;
	private final PropertyGetWithCacheService cacheService;

	public VoucherDeleteService(@Lazy VoucherService voucherService, @Lazy StackVoucherService stackBasicService,
			@Lazy WebServiceEjecucionSvc taskService, @Lazy PropertyGetWithCacheService cacheService) {
		this.voucherService = voucherService;
		this.stackBasicService = stackBasicService;
		this.taskService = taskService;
		this.cacheService = cacheService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse callById(String pVoucherId, String pToken) throws ServerException {
		VoucherDTO _voucher = voucherService.getById(pVoucherId);
		if (_voucher == null)
			throw new ServerException("No se encontro un voucher con el id " + pVoucherId);
		_voucher.setDeleteDate(new Date());
		_voucher.setState(SharedConstants.STATE_INACTIVE);
		voucherService.update(_voucher);

		StackVoucherFilterDTO _filter = new StackVoucherFilterDTO();
		_filter.setVoucher(pVoucherId);
		_filter.setState(SharedConstants.STATE_ACTIVE);
		StackVoucherDTO stack = stackBasicService.getOne(_filter);

		if (stack == null) {
			stack = new StackVoucherDTO();
			stack.setVoucher(pVoucherId);
			stack.setAction(SharedConstants.STATE_INACTIVE);
			stack.setCreationDate(new Date());
			stackBasicService.save(stack);

		} else {
			stack.setState(SharedConstants.STATE_INACTIVE);
			stackBasicService.update(stack);
		}
		return new SharedIdResponse(_voucher.getKey(), _voucher.getCode());
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void callByDocument(String pDocumentId, String pTemplate, String pToken) throws ServerException {

		List<PropiedadDTO> _prop = cacheService.getByValueWithoutField(PropiedadValorDefinidoDTO.API_SERVICE,
				Propiedades.TEMPLATE_VOUCHER, pTemplate, null);
		if (_prop == null || _prop.isEmpty())
			return;

		VoucherFilterDTO _filter = new VoucherFilterDTO();
		_filter.setDocument(pDocumentId);
		_filter.setState(SharedConstants.STATE_ACTIVE);
		List<VoucherDTO> _vouchers = voucherService.getMany(_filter);
		for (VoucherDTO voucherDTO : _vouchers) {
			callById(voucherDTO.getKey(), pToken);
		}

		for (PropiedadDTO propiedadDTO : _prop) {
			WebServiceEjecucionDTO _service = taskService.getServiceVoucherActive(propiedadDTO.getCampo(), pDocumentId);
			if (_service != null) {
				_service.setEstado(SharedConstants.STATE_INACTIVE);
				_service.setFechaEjecucion(new Date());
				_service.setError("Documento eliminado, se inactiva el servicio de voucher asociado");
				taskService.update(_service);
			}
		}

	}

}
