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
import com.accounting.voucher.domain.AccountRecordAuxiliarDTO;
import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.Voucher;
import com.accounting.voucher.domain.VoucherDTO;
import com.accounting.voucher.domain.VoucherLine;
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

		List<VoucherLine> lines = new ArrayList<>();
		for (VoucherLineRequest accountRecordDTO : _item.getLines()) {
			AccountRecordDTO line = new AccountRecordDTO();
			line.setAccount(accountRecordDTO.getAccount());
			line.setNegative(accountRecordDTO.getCredit());
			line.setPositive(accountRecordDTO.getDebit());
			line.setNote(accountRecordDTO.getNote());
			
			VoucherLine _line = new VoucherLine();
			_line.setLine(line);
			
			if (accountRecordDTO.getReferences() != null && !accountRecordDTO.getReferences().isEmpty()) {
				_line.setReferences(new ArrayList<>());
				for (VoucherLineDimensionRequest iReference : accountRecordDTO.getReferences()) {
					AccountRecordAuxiliarDTO _auxiliar = new AccountRecordAuxiliarDTO();
					_auxiliar.setAuxiliarCode(iReference.getCode());
					_auxiliar.setAuxiliarDocumentId(iReference.getDocumentId());
					_auxiliar.setAuxiliarName(iReference.getName());
					_auxiliar.setAuxiliarType(iReference.getAuxiliar());

					AccountDTO accountReference = findAccount(_item.getCatalog(), iReference.getCode(), accountRecordDTO.getAccount());
					if (accountReference == null) {
						accountReference = new AccountDTO();
						accountReference.setDocument(iReference.getDocumentId());
						accountReference.setCatalog(_item.getCatalog());
						accountReference.setCode(iReference.getCode().toUpperCase());
						accountReference.setName(iReference.getName());
						accountReference.setParent(accountRecordDTO.getAccount());
						accountReference.setType(AccountConst.TYPE_AUXILIAR);
						accountReference = createAccountService.call(accountReference);
					}
					_auxiliar.setAccount(accountReference.getKey());
					
					_line.getReferences().add(_auxiliar);
				}
			}
			
			lines.add(_line);
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
		if (type.getService()!=null && item.getDocument() == null)
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
            throw new ServerException("La cuenta (" + account.getCode() + ") " + account.getName() + " no es operativa, revisa que la cuenta no sea un auxiliar o un grupo");
		
		if (lineVO.getReferences() != null && !lineVO.getReferences().isEmpty()) {
			for (VoucherLineDimensionRequest reference : lineVO.getReferences()) {
				if (reference.getCode() == null || reference.getCode().isEmpty())
					throw new ServerException("Estamos creando los auxiliares " +reference.getAuxiliar() +" de " + account.getCode() + " Necesitamos un codigo para relacionar la cuenta, gracias");
				if (reference.getName() == null || reference.getCode().isEmpty())
					throw new ServerException("Estamos creando el auxiliar "+ reference.getCode() + " de " + account.getCode() + " Necesitamos el nombre de la cuenta para crearla");
				if (reference.getDocumentId() == null || reference.getCode().isEmpty())
					throw new ServerException("Estamos creando el auxiliar "+ reference.getCode() + " de " + account.getCode() + " Necesitamos un id de documento para relacionar la cuenta, gracias");
				if (reference.getDocumentId().length() > 32 )
					throw new ServerException("Estamos creando el auxiliar "+ reference.getCode() + " de " + account.getCode() + " El id de documento es un identificador a un documento del sistema no puede tener mas de 32 caracteres, gracias");
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
