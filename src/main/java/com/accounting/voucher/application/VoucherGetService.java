package com.accounting.voucher.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.voucher.application.base.AccountRecordService;
import com.accounting.voucher.application.base.VoucherService;
import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.AccountRecordFilterDTO;
import com.accounting.voucher.domain.Voucher;
import com.accounting.voucher.domain.VoucherDTO;
import com.accounting.voucher.domain.VoucherFilterDTO;
import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

@Service
public class VoucherGetService {
	
	@Autowired @Lazy 
	private VoucherService voucherService;
	@Autowired @Lazy 
	private CatalogService catalogService;
	@Autowired @Lazy 
	private AccountRecordService recordService;

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
	
	public Voucher getById(String catalogId, String voucherId) throws ServerException {
		CatalogDTO catalog = getCatalog(catalogId);
		VoucherFilterDTO filter = new VoucherFilterDTO();
		filter.setCatalog(catalogId);
		filter.setCatalogCode(catalog.getCode());
		filter.setState(SharedConstants.STATE_ACTIVE);
		filter.setKey(voucherId);
		Voucher voucher = new Voucher();
		voucher.setHeader(voucherService.getOne(filter));
		voucher.setRecords(getRecords(voucherId));
		return voucher;
	}

	private List<AccountRecordDTO> getRecords(String voucherId) throws ServerException {
		AccountRecordFilterDTO filter = new AccountRecordFilterDTO();
		filter.setState(SharedConstants.STATE_ACTIVE);
		filter.setVoucher(voucherId);
		return recordService.getMany(filter);
	}
	
	

}
