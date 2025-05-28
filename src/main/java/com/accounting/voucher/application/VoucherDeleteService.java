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
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class VoucherDeleteService {

	@Autowired
	@Lazy
	private PropiedadSvc propiedadService;
	@Autowired @Lazy 
	private VoucherService voucherService;
	@Autowired
	@Lazy
	private StackVoucherService stackBasicService;
	
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
			stackBasicService.save(stack);
		} else {
			stack.setState(SharedConstants.STATE_INACTIVE);
			stackBasicService.update(stack);
		}
		return new SharedIdResponse(_voucher.getKey(), _voucher.getCode());
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse callByDocument(String pDocumentId, String pTemplateId, String pToken) throws ServerException {
		List<PropiedadDTO> _prop =  propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, pTemplateId, Propiedades.TEMPLATE_VOUCHER, null);
		if(_prop == null || _prop.isEmpty()) return null;
		
		VoucherFilterDTO _filter = new VoucherFilterDTO();
		_filter.setDocument(pDocumentId);
		_filter.setState(SharedConstants.STATE_ACTIVE);
		List<VoucherDTO> _vouchers = voucherService.getMany(_filter);
		for (VoucherDTO voucherDTO : _vouchers) {
			callById(voucherDTO.getKey(), pToken);
		}
		return null;
	}
	
}
