package com.accounting.api.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.accounting.api.domain.VoucherLineDimensionRequest;
import com.accounting.api.domain.VoucherLineRequest;
import com.accounting.api.domain.VoucherRequest;
import com.accounting.plan.application.PlanCreateAccountService;
import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.application.base.TypeService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.AccountFilterDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.CatalogFilterDTO;
import com.accounting.plan.domain.TypeDTO;
import com.accounting.plan.domain.TypeFilterDTO;
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

	@Autowired
	@Lazy
	private VoucherCreateService createService;
	@Autowired
	@Lazy
	private CatalogService catalogService;
	@Autowired
	@Lazy
	private AccountService accountService;
	@Autowired
	@Lazy
	private TypeService typeService;
	@Autowired
	@Lazy
	private PlanCreateAccountService createAccountService;

	public SharedIdResponse call(SharedToken _token, VoucherRequest _item) throws ServerException {
		validateItem(_item);

		Voucher voucher = new Voucher();

		VoucherDTO header = new VoucherDTO();

		header.setCatalog(_item.getCatalog());
		header.setConcept(_item.getConcept());
		header.setFactDate(_item.getFactDate());
		header.setDocument(_item.getDocument());
		header.setValue(_item.getValue());
		header.setType(_item.getType());
		voucher.setHeader(header);

		List<AccountRecordDTO> lines = new ArrayList<>();
		for (VoucherLineRequest accountRecordDTO : _item.getLines()) {
			AccountRecordDTO line = new AccountRecordDTO();
			line.setAccount(accountRecordDTO.getAccount());
			line.setNegative(accountRecordDTO.getCredit());
			line.setPositive(accountRecordDTO.getDebit());
			line.setNote(accountRecordDTO.getNote());
			
			//Esto toca mejorarlo
			if (accountRecordDTO.getReferences() != null && !accountRecordDTO.getReferences().isEmpty()) {
				line.setThird(accountRecordDTO.getReferences().get(0).getCode());
				line.setThirdId(accountRecordDTO.getReferences().get(0).getDocumentId());
				line.setThirdName(accountRecordDTO.getReferences().get(0).getName());
				if(accountRecordDTO.getReferences().size() > 1) {
                    line.setCenter(accountRecordDTO.getReferences().get(1).getCode());
                    line.setCenterId(accountRecordDTO.getReferences().get(1).getDocumentId());
                    line.setCenterName(accountRecordDTO.getReferences().get(1).getName());
				}
			}
			lines.add(line);
		}
		voucher.setRecords(lines);

		return createService.call(voucher, _token);

	}

	private void validateItem(VoucherRequest item) throws ServerException {
		if (item.getCatalog() == null || item.getCatalog().isEmpty())
			throw new ServerException("El codigo del catalogo no se reconoce");
		if (item.getType() == null || item.getType().isEmpty())
			throw new ServerException("No se encuentra el tipo de documento");

		if (item.getConcept() == null || item.getConcept().isEmpty())
			throw new ServerException("No se registra el concepto");

		CatalogFilterDTO filter = new CatalogFilterDTO();
		filter.setCode(item.getCatalog().toUpperCase());
		filter.setState(SharedConstants.STATE_ACTIVE);
		CatalogDTO catalog = catalogService.getOne(filter);

		if (catalog == null)
			throw new ServerException("No se reconoce el catalogo con ese codigo");
		if (item.getLines() == null || item.getLines().isEmpty())
			throw new ServerException("El documento no tiene campos, recuerda usar el tag lines");

		TypeFilterDTO typeFilter = new TypeFilterDTO();
		typeFilter.setCatalog(catalog.getKey());
		typeFilter.setCode(item.getType().toUpperCase());
		typeFilter.setState(SharedConstants.STATE_ACTIVE);
		TypeDTO type = typeService.getOne(typeFilter);
		if (type == null)
			throw new ServerException("No se reconoce el tipo de documento");
		if (type.getAutomatic() && item.getDocument() == null)
			throw new ServerException("El tipo de documento es automatico y no se ha enviado el documento");

		item.setCatalog(catalog.getKey());
		item.setType(type.getKey());

		for (int i = 0; i < item.getLines().size(); i++) {
			VoucherLineRequest lineVO = item.getLines().get(i);
			if (lineVO.getAccount() == null)
				throw new ServerException("La linea " + i + " no tiene el codigo de la cuenta");
			AccountDTO account = getAccount(catalog, lineVO);
			lineVO.setAccount(account.getKey());
		}
	}

	private AccountDTO getAccount(CatalogDTO catalog, VoucherLineRequest lineVO) throws ServerException {
		
		AccountDTO account = findAccount(catalog.getKey(), lineVO.getAccount(), null);
		if (account == null)
			throw new ServerException("No se reconoce la cuenta con el codigo " + lineVO.getAccount().toUpperCase());
		if(account.getType().compareTo(AccountConst.TYPE_OPERATIONAL) != 0)
            throw new ServerException("La cuenta " + account.getName() + " no es operativa, revisa que la cuenta no sea un auxiliar o un grupo");
		
		if (lineVO.getReferences() != null && !lineVO.getReferences().isEmpty()) {
			for (VoucherLineDimensionRequest reference : lineVO.getReferences()) {
				AccountDTO accountReference = findAccount(catalog.getKey(), reference.getCode(), account.getKey());
				if (accountReference == null) {
					if (reference.getName() == null)
						throw new ServerException("Estamos creando los auxiliares de " + account.getCode() + " Necesitamos el nombre de la cuenta para crearla");
					if (reference.getDocumentId() == null)
						throw new ServerException("Estamos creando los auxiliares de " + account.getCode() + " Necesitamos un id de documento para relacionar la cuenta, gracias");
					accountReference = new AccountDTO();
					accountReference.setDocument(reference.getDocumentId());
					accountReference.setCatalogDocument(catalog.getDocument());
					accountReference.setCode(reference.getCode().toUpperCase());
					accountReference.setName(reference.getName());
					accountReference.setParent(account.getKey());
					accountReference.setType(AccountConst.TYPE_AUXILIAR);
					accountReference = createAccountService.call(accountReference);
				}
				reference.setCode(accountReference.getKey());
			}
		}
		return account;
	}

	private AccountDTO findAccount(String catalogId, String accountCode, String parentId) throws ServerException {
		AccountFilterDTO filterA = new AccountFilterDTO();
		filterA.setCatalog(catalogId);
		filterA.setParent(parentId);
		filterA.setCode(accountCode.toUpperCase());
		filterA.setState(SharedConstants.STATE_ACTIVE);
		return  accountService.getOne(filterA);
	}

}
