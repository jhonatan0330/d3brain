package d3.accounting_voucher.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.accounting_api.domain.VoucherPrepareRequest;
import d3.accounting_plan.application.base.CatalogService;
import d3.accounting_plan.application.base.TypeService;
import d3.accounting_plan.domain.CatalogDTO;
import d3.accounting_plan.domain.TypeDTO;
import d3.accounting_plan.domain.TypeFilterDTO;
import d3.accounting_voucher.application.base.AccountRecordAuxiliarService;
import d3.accounting_voucher.application.base.AccountRecordService;
import d3.accounting_voucher.application.base.VoucherService;
import d3.accounting_voucher.domain.AccountRecordAuxiliarDTO;
import d3.accounting_voucher.domain.AccountRecordAuxiliarFilterDTO;
import d3.accounting_voucher.domain.AccountRecordDTO;
import d3.accounting_voucher.domain.AccountRecordFilterDTO;
import d3.accounting_voucher.domain.Voucher;
import d3.accounting_voucher.domain.VoucherDTO;
import d3.accounting_voucher.domain.VoucherFilterDTO;
import d3.accounting_voucher.domain.VoucherLine;
import d3.shared.domain.SharedConstants;
import d3.shared.domain.SharedIdResponse;
import d3.shared.domain.SharedToken;
import d3.shared.domain.ServerException;
import org.springframework.context.annotation.Lazy;

@Service
public class VoucherGetService {

	private final VoucherService voucherService;
	private final CatalogService catalogService;
	private final AccountRecordService recordService;
	private final AccountRecordAuxiliarService recordAuxiliarService;
	private final TypeService typeService;

	public VoucherGetService(@Lazy VoucherService voucherService, @Lazy CatalogService catalogService,
			@Lazy AccountRecordService recordService, @Lazy AccountRecordAuxiliarService recordAuxiliarService,
			@Lazy TypeService typeService) {
		this.voucherService = voucherService;
		this.catalogService = catalogService;
		this.recordService = recordService;
		this.recordAuxiliarService = recordAuxiliarService;
		this.typeService = typeService;
	}

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
		filter.setType(type.getKey());
		filter.setState(SharedConstants.STATE_ACTIVE);
		VoucherDTO header = voucherService.getOne(filter);
		if (header == null)
			throw new ServerException("No se encontro un comprobante para este documento y este servicio");

		return new SharedIdResponse(header.getKey(), header.getCode());
	}

	private List<VoucherLine> getRecords(String voucherId) throws ServerException {

		AccountRecordFilterDTO filter = new AccountRecordFilterDTO();
		filter.setState(SharedConstants.STATE_ACTIVE);
		filter.setVoucher(voucherId);
		filter.setEndRow(4000);
		List<AccountRecordDTO> _records = recordService.getMany(filter);
		if (_records == null || _records.isEmpty())
			return null;

		AccountRecordAuxiliarFilterDTO _filter = new AccountRecordAuxiliarFilterDTO();
		_filter.setVoucher(voucherId);
		_filter.setState(SharedConstants.STATE_ACTIVE);
		filter.setEndRow(10000);
		List<AccountRecordAuxiliarDTO> _auxiliares = recordAuxiliarService.getMany(_filter);

		List<VoucherLine> _lines = new ArrayList<>();
		for (AccountRecordDTO accountRecordDTO : _records) {
			VoucherLine _line = new VoucherLine();
			_line.setLine(accountRecordDTO);
			if (_auxiliares != null && !_auxiliares.isEmpty()) {
				for (AccountRecordAuxiliarDTO iAux : _auxiliares) {
					if (iAux.getRecordLine().compareTo(accountRecordDTO.getKey()) == 0) {
						if (_line.getReferences() == null)
							_line.setReferences(new ArrayList<>());
						_line.getReferences().add(iAux);
					}
				}
			}
			_lines.add(_line);
		}
		return _lines;

	}

}
