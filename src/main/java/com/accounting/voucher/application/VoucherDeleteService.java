package com.accounting.voucher.application;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.StackVoucherService;
import com.accounting.plan.domain.StackVoucherDTO;
import com.accounting.plan.domain.StackVoucherFilterDTO;
import com.accounting.voucher.application.base.VoucherService;
import com.accounting.voucher.domain.VoucherDTO;
import com.accounting.voucher.domain.VoucherFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.shared.domain.SharedIdResponse;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.property.application.PropertyGetWithCacheService;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.webservice.application.WebServiceEjecucionSvc;
import com.softure.webservice.domain.WebServiceEjecucionDTO;

@Service
public class VoucherDeleteService {

	@Autowired @Lazy 
	private VoucherService voucherService;
	@Autowired
	@Lazy
	private StackVoucherService stackBasicService;
	@Autowired
	@Lazy
	private WebServiceEjecucionSvc taskService;
	@Autowired @Lazy 
	private PropertyGetWithCacheService cacheService;
	
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse callById(String pVoucherId, String pToken) throws ServerException {
		VoucherDTO _voucher = voucherService.getById(pVoucherId);
		if(_voucher ==null) throw new ServerException("No se encontro un voucher con el id " + pVoucherId);
		_voucher.setDeleteDate(new Date());
		_voucher.setState(SharedConstants.STATE_INACTIVE);
		voucherService.update(_voucher);
		
		StackVoucherFilterDTO _filter = new StackVoucherFilterDTO();
		_filter.setVoucher(pVoucherId);
		_filter.setState(SharedConstants.STATE_ACTIVE);
		StackVoucherDTO stack = stackBasicService.getOne(_filter);
		
		if(stack==null) {
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
		
		List<PropiedadDTO> _prop = cacheService.getByValueWithoutField(PropiedadValorDefinidoDTO.API_SERVICE, Propiedades.TEMPLATE_VOUCHER, pTemplate, null);
		if(_prop == null || _prop.isEmpty()) return;
		
		VoucherFilterDTO _filter = new VoucherFilterDTO();
		_filter.setDocument(pDocumentId);
		_filter.setState(SharedConstants.STATE_ACTIVE);
		List<VoucherDTO> _vouchers = voucherService.getMany(_filter);
		for (VoucherDTO voucherDTO : _vouchers) {
			callById(voucherDTO.getKey(), pToken);
		}
		
		for (PropiedadDTO propiedadDTO : _prop) {
			WebServiceEjecucionDTO _service = taskService.getServiceVoucherActive( propiedadDTO.getCampo(), pDocumentId);
			if (_service != null) {
				_service.setEstado(SharedConstants.STATE_INACTIVE);
				_service.setFechaEjecucion(new Date());
				_service.setError("Documento eliminado, se inactiva el servicio de voucher asociado");
				taskService.update(_service);	
			}
		}
		
	}
	
}
