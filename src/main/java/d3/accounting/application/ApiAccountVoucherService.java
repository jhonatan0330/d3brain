package d3.accounting.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.accounting.application.base.CatalogService;
import d3.accounting.domain.AccountConst;
import d3.accounting.domain.AccountDTO;
import d3.accounting.domain.AccountRecordAuxiliarDTO;
import d3.accounting.domain.AccountRecordDTO;
import d3.accounting.domain.CatalogDTO;
import d3.accounting.domain.CatalogFilterDTO;
import d3.accounting.domain.TypeDTO;
import d3.accounting.domain.Voucher;
import d3.accounting.domain.VoucherDTO;
import d3.accounting.domain.VoucherLine;
import d3.accounting.domain.VoucherLineDimensionRequest;
import d3.accounting.domain.VoucherLineRequest;
import d3.accounting.domain.VoucherRequest;
import d3.document.application.PedidoVentaSvc;
import d3.document.domain.PedidoVentaDTO;
import d3.shared.application.D3Utils;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.shared.domain.SharedIdResponse;
import d3.shared.domain.SharedToken;

@Service
public class ApiAccountVoucherService {

	private final VoucherCreateService createService;
	private final CatalogService catalogService;
	private final PlanGetAccountService accountService;
	private final PrepareTypeToCatalogService typeService;
	private final PlanCreateAccountService createAccountService;
	private final PedidoVentaSvc documentService;

	public ApiAccountVoucherService(@Lazy VoucherCreateService createService, @Lazy CatalogService catalogService,
			@Lazy PlanGetAccountService accountService, @Lazy PrepareTypeToCatalogService typeService,
			@Lazy PlanCreateAccountService createAccountService, @Lazy PedidoVentaSvc documentService) {
		this.createService = createService;
		this.catalogService = catalogService;
		this.accountService = accountService;
		this.typeService = typeService;
		this.createAccountService = createAccountService;
		this.documentService = documentService;
	}

	public SharedIdResponse call(SharedToken _token, VoucherRequest _item) throws ServerException {
		validateItem(_item, _token);

		Voucher voucher = new Voucher();

		VoucherDTO header = new VoucherDTO();

		header.setCatalog(_item.getCatalog());
		header.setConcept(_item.getConcept());
		header.setFactDate(_item.getFactDate());
		header.setDocument(_item.getDocument());
		header.setMainDocument(_item.getMainDocument());
		header.setValue(_item.getValue());
		header.setType(_item.getType());
		voucher.setHeader(header);

		List<VoucherLine> lines = new ArrayList<>();
		Map<String, AccountDTO> _accountMap = new java.util.HashMap<String, AccountDTO>();
		for (VoucherLineRequest accountRecordDTO : _item.getLines()) {
			AccountRecordDTO line = new AccountRecordDTO();
			line.setAccount(accountRecordDTO.getAccount());
			line.setNegative(accountRecordDTO.getCredit());
			line.setPositive(accountRecordDTO.getDebit());
			line.setNote(accountRecordDTO.getNote());
			line.setType(accountRecordDTO.getType());
			line.setMainDocument(accountRecordDTO.getMainDocument());
			line.setAccountLink(accountRecordDTO.getAccountLink());

			VoucherLine _line = new VoucherLine();
			_line.setLine(line);

			if (accountRecordDTO.getReferences() != null && !accountRecordDTO.getReferences().isEmpty()) {
				_line.setReferences(new ArrayList<>());
				for (VoucherLineDimensionRequest iReference : accountRecordDTO.getReferences()) {

					AccountDTO _accountReference = null;
					String _referenceString = D3Utils
							.formatFunction(iReference.getDocumentId() + "_" + accountRecordDTO.getAccount());
					if (_accountMap.containsKey(_referenceString)) {
						_accountReference = _accountMap.get(_referenceString);
					} else {
						_accountReference = accountService.findAccountByDocumentId(_item.getCatalog(),
								iReference.getDocumentId(), accountRecordDTO.getAccount());
						if (_accountReference != null)
							_accountMap.put(_referenceString, _accountReference);
					}

					if (_accountReference == null) {
						_accountReference = createAccountService.createAuxiliarAccount(_item,
								accountRecordDTO.getAccount(), iReference.getDocumentId());
						_accountMap.put(_referenceString, _accountReference);
					}

					AccountRecordAuxiliarDTO _auxiliar = new AccountRecordAuxiliarDTO();
					_auxiliar.setAuxiliarDocumentId(iReference.getDocumentId());
					_auxiliar.setAuxiliarType(iReference.getAuxiliar());
					_auxiliar.setAccount(_accountReference.getKey());
					_line.getReferences().add(_auxiliar);
				}
			}

			lines.add(_line);
		}
		voucher.setRecords(lines);
		return createService.call(voucher, _token);
	}

	private void validateItem(VoucherRequest item, SharedToken pToken) throws ServerException {

		if (item.getCatalog() == null || item.getCatalog().isEmpty())
			throw new ServerException("El codigo del catalogo no se reconoce");
		// if (item.getType() == null || item.getType().isEmpty())
		// throw new ServerException("No se encuentra el tipo de documento");

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

		TypeDTO type = typeService.call(item.getType(), catalog.getKey(), pToken);

		if (type.getService() != null && (item.getDocument() == null || item.getDocument().isEmpty()))
			throw new ServerException("El tipo de documento es automatico y no se ha enviado el documento");

		if (item.getDocument() != null) {
			PedidoVentaDTO _document = documentService.consultaXId(item.getDocument());
			if (_document == null)
				throw new ServerException("No existe el documento " + item.getDocument());
		}

		item.setCatalog(catalog.getKey());
		item.setType(type.getKey());

		Map<String, AccountDTO> _accountMap = new java.util.HashMap<String, AccountDTO>();
		for (int i = 0; i < item.getLines().size(); i++) {
			VoucherLineRequest lineVO = item.getLines().get(i);
			if (lineVO.getAccount() == null)
				throw new ServerException("La linea " + i + " no tiene el codigo de la cuenta");
			AccountDTO account = getAccount(catalog, lineVO, _accountMap);
			lineVO.setAccount(account.getKey());
		}

	}

	private AccountDTO getAccount(CatalogDTO catalog, VoucherLineRequest lineVO, Map<String, AccountDTO> pAccountMap)
			throws ServerException {
		AccountDTO account = null;
		if (pAccountMap.containsKey(lineVO.getAccount().toUpperCase())) {
			account = pAccountMap.get(lineVO.getAccount().toUpperCase());
		} else {
			account = accountService.findAccountByCode(catalog.getKey(), lineVO.getAccount(), null);
			if (account == null)
				throw new ServerException(
						"No se reconoce la cuenta con el codigo " + lineVO.getAccount().toUpperCase());
			if (account.getType().compareTo(AccountConst.TYPE_OPERATIONAL) != 0)
				throw new ServerException("La cuenta (" + account.getCode() + ") " + account.getName()
						+ " no es operativa, revisa que la cuenta no sea un auxiliar o un grupo");
			pAccountMap.put(lineVO.getAccount().toUpperCase(), account);
		}

		if (lineVO.getReferences() != null && !lineVO.getReferences().isEmpty()) {
			for (VoucherLineDimensionRequest reference : lineVO.getReferences()) {
				if (reference.getDocumentId() == null || reference.getDocumentId().isEmpty())
					reference.setDocumentId(AccountConst.AUXILIAR_EMPTY);
				if (reference.getDocumentId() != null && reference.getDocumentId().length() > 32)
					throw new ServerException("Estamos creando el auxiliar " + reference.getDocumentId() + " de "
							+ account.getCode()
							+ " El id de documento es un identificador a un documento del sistema no puede tener mas de 32 caracteres, gracias");
			}
		}
		return account;
	}

}
