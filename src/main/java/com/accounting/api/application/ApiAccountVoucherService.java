package com.accounting.api.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.accounting.api.domain.VoucherLineRequest;
import com.accounting.api.domain.VoucherRequest;
import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.accounting.voucher.application.VoucherCreateService;
import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.Voucher;
import com.accounting.voucher.domain.VoucherDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.shared.domain.SharedIdResponse;
import com.shared.domain.SharedToken;

@Service
public class ApiAccountVoucherService {

	@Autowired @Lazy private VoucherCreateService createService;
	@Autowired @Lazy private CatalogService catalogService;
	@Autowired @Lazy private AccountService accountService;

	public SharedIdResponse call(SharedToken _token, VoucherRequest _item) throws ServerException {
		validateItem(_item);
		
		Voucher voucher = new Voucher();
		
		VoucherDTO header = new VoucherDTO();
		
		header.setCatalog(_item.getCatalog());
		header.setConcept(_item.getConcept());
		header.setFactDate(_item.getFactDate());
		header.setValue(_item.getValue());
		voucher.setHeader(header);
		
		List<AccountRecordDTO> lines = new ArrayList<>();
		for (VoucherLineRequest accountRecordDTO : _item.getLines()) {
			AccountRecordDTO line = new AccountRecordDTO();
			line.setAccount(accountRecordDTO.getAccount());
			line.setNegative(accountRecordDTO.getCredit());
			line.setPositive(accountRecordDTO.getDebit());
			lines.add(line);
		}
		voucher.setRecords(lines);
		
		return createService.call(voucher, _token);
				 
	}

	private void validateItem(VoucherRequest item) throws ServerException {
		if (item.getCatalog() == null || item.getCatalog().isEmpty())
			throw new ServerException("El codigo del catalogo no se reconoce");
		
		CatalogFilterDTO filter = new CatalogFilterDTO();
		filter.setCode(item.getCatalog().toUpperCase());
		filter.setState(SharedConstants.STATE_ACTIVE);
		CatalogDTO catalog = catalogService.getOne(filter);
		
		if (catalog == null )
			throw new ServerException("No se reconoce el catalogo con ese codigo");
		
		item.setCatalog(catalog.getKey());
		
		if (item.getLines() == null || item.getLines().isEmpty())
			throw new ServerException("El documento no tiene campos, recuerda usar el tag lines");
		for (int i = 0; i < item.getLines().size(); i++) {
			VoucherLineRequest lineVO = item.getLines().get(i);
			if (lineVO.getAccount() == null)
				throw new ServerException("La linea " + i + " no tiene el codigo de la cuenta");
		
			AccountFilterDTO filterA = new AccountFilterDTO();
			filterA.setCatalog(catalog.getKey());
			filterA.setCode(lineVO.getAccount().toUpperCase());
			filterA.setState(SharedConstants.STATE_ACTIVE);
			
			AccountDTO account = accountService.getOne(filterA);
			if (account == null )
				throw new ServerException("No se reconoce la cuenta con ese codigo");
			lineVO.setAccount(account.getKey());
			
		}
	}


}
