package com.accounting.voucher.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.api.domain.VoucherPrepareRequest;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.application.base.TypeService;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.TypeDTO;
import com.accounting.plan.domain.TypeFilterDTO;
import com.accounting.voucher.application.base.AccountRecordService;
import com.accounting.voucher.application.base.VoucherService;
import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.AccountRecordFilterDTO;
import com.accounting.voucher.domain.Voucher;
import com.accounting.voucher.domain.VoucherDTO;
import com.accounting.voucher.domain.VoucherFilterDTO;
import com.shared.domain.SharedConstants;
import com.shared.domain.SharedIdResponse;
import com.shared.domain.SharedToken;
import com.shared.domain.ServerException;

@Service
public class VoucherGetService {
	
	@Autowired @Lazy 
	private VoucherService voucherService;
	@Autowired @Lazy 
	private CatalogService catalogService;
	@Autowired @Lazy 
	private AccountRecordService recordService;
	@Autowired @Lazy 
	private TypeService typeService;

	public List<VoucherDTO> call(String catalogId) throws ServerException {
		CatalogDTO catalog = getCatalog(catalogId);
		VoucherFilterDTO filter = new VoucherFilterDTO();
		filter.setCatalog(catalogId);
		filter.setCatalogCode(catalog.getCode());
		filter.setState(SharedConstants.STATE_ACTIVE);
		return voucherService.getMany(filter);
	}
	
	private CatalogDTO getCatalog(String catalogId) throws ServerException {
		if (catalogId == null)
			throw new ServerException("Es importante identificar el catalogo para guardar el comprobante");
		CatalogDTO catalogDTO = catalogService.getById(catalogId);
		if (catalogDTO == null)
			throw new ServerException("No se encontro un catalogo con ese identificador");
		return catalogDTO;
	}
	
	public Voucher getById(String voucherId) throws ServerException {
		Voucher voucher = new Voucher();
		voucher.setHeader(voucherService.getById(voucherId));
		voucher.setRecords(getRecords(voucherId));
		return voucher;
	}
	
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse getByDocument(VoucherPrepareRequest pItem, SharedToken pToken) throws ServerException {

		TypeFilterDTO _typeFilter = new TypeFilterDTO();
		_typeFilter.setService(pItem.getServiceId());
		_typeFilter.setState(SharedConstants.STATE_ACTIVE);
		TypeDTO type = typeService.getOne(_typeFilter);
		if (type == null)
			throw new ServerException("No se encontro un tipo de comprobante con ese identificador");
		
		VoucherFilterDTO filter = new VoucherFilterDTO();
		filter.setType(null);
		filter.setDocument(pItem.getDocumentId());
		filter.setDocument(type.getKey());
		filter.setState(SharedConstants.STATE_ACTIVE);
		VoucherDTO header = voucherService.getOne(filter);
		if (header == null)
			throw new ServerException("No se encontro un comprobante para este documento y este servicio");
		
		return new SharedIdResponse(header.getKey(), header.getCode());
	}

	private List<AccountRecordDTO> getRecords(String voucherId) throws ServerException {
		AccountRecordFilterDTO filter = new AccountRecordFilterDTO();
		filter.setState(SharedConstants.STATE_ACTIVE);
		filter.setVoucher(voucherId);
		return recordService.getMany(filter);
	}
	
}
