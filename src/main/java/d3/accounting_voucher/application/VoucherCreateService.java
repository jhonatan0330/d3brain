package d3.accounting_voucher.application;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.accounting_plan.application.PlanCreateCatalogService;
import d3.accounting_plan.application.base.AccountService;
import d3.accounting_plan.application.base.CatalogService;
import d3.accounting_plan.application.base.StackVoucherService;
import d3.accounting_plan.application.base.TypeService;
import d3.accounting_plan.domain.AccountConst;
import d3.accounting_plan.domain.AccountDTO;
import d3.accounting_plan.domain.CatalogDTO;
import d3.accounting_plan.domain.StackVoucherDTO;
import d3.accounting_plan.domain.TypeDTO;
import d3.accounting_voucher.application.base.AccountRecordAuxiliarService;
import d3.accounting_voucher.application.base.AccountRecordService;
import d3.accounting_voucher.application.base.VoucherService;
import d3.accounting_voucher.domain.AccountRecordAuxiliarDTO;
import d3.accounting_voucher.domain.Voucher;
import d3.accounting_voucher.domain.VoucherDTO;
import d3.accounting_voucher.domain.VoucherFilterDTO;
import d3.accounting_voucher.domain.VoucherLine;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.shared.domain.SharedIdResponse;
import d3.shared.domain.SharedToken;
import d3.process_form.application.ConsecutivoSvc;
import d3.process_form.domain.ConsecutivoDTO;

@Service
public class VoucherCreateService {

	private final CatalogService catalogService;
	private final AccountService accountService;
	private final VoucherService voucherService;
	private final AccountRecordService recordService;
	private final AccountRecordAuxiliarService auxiliarService;
	private final ConsecutivoSvc consecutiveService;
	private final StackVoucherService stackBasicService;
	private final TypeService typeService;
	private final PlanCreateCatalogService createCatalogService;

	public VoucherCreateService(@Lazy CatalogService catalogService, @Lazy AccountService accountService,
			@Lazy VoucherService voucherService, @Lazy AccountRecordService recordService,
			@Lazy AccountRecordAuxiliarService auxiliarService, @Lazy ConsecutivoSvc consecutiveService,
			@Lazy StackVoucherService stackBasicService, @Lazy TypeService typeService,
			@Lazy PlanCreateCatalogService createCatalogService) {
		this.catalogService = catalogService;
		this.accountService = accountService;
		this.voucherService = voucherService;
		this.recordService = recordService;
		this.auxiliarService = auxiliarService;
		this.consecutiveService = consecutiveService;
		this.stackBasicService = stackBasicService;
		this.typeService = typeService;
		this.createCatalogService = createCatalogService;
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(Voucher _voucher, SharedToken token) throws ServerException {
		CatalogDTO catalogDTO = getCatalog(_voucher.getHeader());
		validateInfoHeaderAndRecords(_voucher, token, catalogDTO.getCode());

		configureAccounts(_voucher, catalogDTO);
		voucherService.save(_voucher.getHeader());
		VoucherDTO headerDTO = getVoucherById(catalogDTO.getCode(), _voucher.getHeader().getKey());
		saveRecords(catalogDTO.getCode(), _voucher, headerDTO.getKey());
		stackVoucher(headerDTO.getKey());
		createCatalogService.validateTemporalFrame(_voucher.getHeader().getFactDate());
		// Esto lo retiro por el momenot miestras esten junto //el codigo al final para
		// evitar errores en transaccionalidad
		// getCodeVoucher(catalogDTO, headerDTO, token.getToken());
		return new SharedIdResponse(headerDTO.getKey(), headerDTO.getCode());
	}

	private void stackVoucher(String voucherId) throws ServerException {
		StackVoucherDTO stack = new StackVoucherDTO();
		stack.setVoucher(voucherId);
		stack.setCreationDate(new Date());
		stackBasicService.save(stack);
	}

	private void configureAccounts(Voucher _voucher, CatalogDTO catalogDTO) throws ServerException {
		for (VoucherLine item : _voucher.getRecords()) {
			AccountDTO account = accountService.getById(item.getLine().getAccount());
			if (account == null)
				throw new ServerException("La cuenta no existe en la base de datos");
			if (account.getCatalog().compareTo(catalogDTO.getKey()) != 0)
				throw new ServerException("La cuenta no pertenece al catalogo. " + account.getName());
			if (account.getState().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException("La cuenta no se encuentra activa. " + account.getName());
			// createMapLine(catalogDTO, account);

			if (item.getReferences() != null && !item.getReferences().isEmpty()) {
				for (AccountRecordAuxiliarDTO iAuxiliar : item.getReferences()) {
					AccountDTO third = accountService.getById(iAuxiliar.getAccount());
					if (third == null)
						throw new ServerException(
								"El auxiliar " + iAuxiliar.getAuxiliarType() + " no existe en la base de datos");
					if (third.getCatalog().compareTo(catalogDTO.getKey()) != 0)
						throw new ServerException("El auxiliar " + iAuxiliar.getAuxiliarType()
								+ " no pertenece al catalogo. " + third.getName());
					if (third.getState().compareTo(SharedConstants.STATE_ACTIVE) != 0)
						throw new ServerException("El auxiliar " + iAuxiliar.getAuxiliarType()
								+ " no se encuentra activo. " + third.getName());
				}

			}

		}
	}

	private void saveRecords(String catalogCode, Voucher _voucher, String headerId) throws ServerException {
		for (VoucherLine item : _voucher.getRecords()) {
			if (item.getLine().getAccount() != null) {
				item.getLine().setVoucher(headerId);
				item.getLine().setCatalogCode(catalogCode);
				recordService.save(item.getLine());

				if (item.getReferences() != null) {
					for (AccountRecordAuxiliarDTO iAux : item.getReferences()) {
						iAux.setVoucher(headerId);
						iAux.setRecordLine(item.getLine().getKey());
						auxiliarService.save(iAux);
					}
				}
			}
		}
	}

	private void validateInfoHeaderAndRecords(Voucher _voucher, SharedToken token, String catalogoCode)
			throws ServerException {
		if (_voucher == null)
			throw new ServerException("Es en serio no enviaste informacion");
		if (_voucher.getHeader() == null)
			throw new ServerException("Te hace falta la informacion de encabezado del comprobante");
		if (_voucher.getRecords() == null || _voucher.getRecords().isEmpty())
			throw new ServerException("Es curiosos pero no enviaste registros de cuentas");
		// if (_voucher.getHeader().getValue() == null ||
		// _voucher.getHeader().getValue().compareTo(BigDecimal.ZERO) == 0)
		// throw new ServerException("El valor total del comprobante no esta
		// diligenciado");
		BigDecimal valueAllRecords = BigDecimal.ZERO;
		BigDecimal valueAllRecordsPositive = BigDecimal.ZERO;
		BigDecimal valueAllRecordsNegative = BigDecimal.ZERO;

		List<VoucherLine> toRemove = new ArrayList<>();
		for (VoucherLine iVoucherLine : _voucher.getRecords()) {

			if (iVoucherLine.getLine().getAccount() != null && iVoucherLine.getLine().getAccount().isEmpty())
				iVoucherLine.getLine().setAccount(null);
			if (iVoucherLine.getLine().getPositive() == null)
				iVoucherLine.getLine().setPositive(BigDecimal.ZERO);
			if (iVoucherLine.getLine().getNegative() == null)
				iVoucherLine.getLine().setNegative(BigDecimal.ZERO);
			iVoucherLine.getLine()
					.setValue(iVoucherLine.getLine().getPositive().add(iVoucherLine.getLine().getNegative().negate()));
			if (iVoucherLine.getLine().getAccount() == null
					&& iVoucherLine.getLine().getValue().compareTo(BigDecimal.ZERO) != 0)
				throw new ServerException("Existe un registro con valor " + iVoucherLine.getLine().getValue()
						+ " pero no tiene una cuenta asignada");
			if (iVoucherLine.getLine().getAccount() != null
					&& iVoucherLine.getLine().getValue().compareTo(BigDecimal.ZERO) == 0)
				throw new ServerException("Existe un registro sin valor pero no tiene una cuenta asignada");
			if (iVoucherLine.getLine().getAccount() == null
					&& iVoucherLine.getLine().getValue().compareTo(BigDecimal.ZERO) == 0) {
				toRemove.add(iVoucherLine);
			} else {
				if (iVoucherLine.getLine().getType() == null
						|| iVoucherLine.getLine().getType().compareTo(AccountConst.TYPE_RECORD_RECLASIFICATION) != 0)
					valueAllRecords = valueAllRecords.add(iVoucherLine.getLine().getPositive());
				valueAllRecordsPositive = valueAllRecordsPositive.add(iVoucherLine.getLine().getPositive());
				valueAllRecordsNegative = valueAllRecordsNegative.add(iVoucherLine.getLine().getNegative());
				if (iVoucherLine.getLine().getNote() != null && iVoucherLine.getLine().getNote().isEmpty())
					iVoucherLine.getLine().setNote(null);
				iVoucherLine.getLine().setFactDate(_voucher.getHeader().getFactDate());
			}
			if (iVoucherLine.getReferences() != null && !iVoucherLine.getReferences().isEmpty()) {
				for (AccountRecordAuxiliarDTO iReference : iVoucherLine.getReferences()) {

					if (iReference.getAuxiliarType() == null || iReference.getAuxiliarType().isEmpty())
						throw new ServerException(
								"Existe un registro con valor " + iVoucherLine.getLine().getAccountCode()
										+ " con una referencia auxiliar que no tiene el tipo");

					if (iReference.getAuxiliarDocumentId() != null && iReference.getAuxiliarDocumentId().isEmpty())
						iReference.setAuxiliarDocumentId(null);
				}
			}
		}

		TypeDTO type = typeService.getById(_voucher.getHeader().getType());
		if (type == null)
			throw new ServerException("No se reconoce el tipo de documento");
		if (type.getService() != null && _voucher.getHeader().getDocument() == null)
			throw new ServerException("El tipo de documento es automatico y no se ha enviado el documento");

		if (type.getPattern().compareTo(AccountConst.TYPE_PATTERN_COMPROBANTE) == 0) {
			if (_voucher.getHeader().getValue().compareTo(valueAllRecords) != 0)
				throw new ServerException("El valor total del comprobante (" + _voucher.getHeader().getValue()
						+ ") no concuerda con los valores positivos de los registros (" + valueAllRecords
						+ "), sin tener en cuenta los de reclasificacion");
			if (valueAllRecordsNegative.compareTo(valueAllRecordsPositive) != 0)
				throw new ServerException("El valor de los valores negativos (" + valueAllRecordsNegative
						+ ") no concuerda con los valores positivos de los registros (" + valueAllRecordsPositive
						+ "), hay una diferencia de " + valueAllRecordsPositive.add(valueAllRecordsNegative.negate()));
		}

		ConsecutivoDTO consecutive = null;
		if (type.getConsecutive() == null) {
			ConsecutivoDTO newConsecutive = new ConsecutivoDTO();
			newConsecutive.setNombre(type.getName());
			newConsecutive.setPrefijo(catalogoCode + "_" + type.getCode() + "_");
			newConsecutive.setNumeroInicial(new BigDecimal(1000));
			newConsecutive.setNumeroActual(new BigDecimal(1000));
			consecutive = consecutiveService.guardar(newConsecutive, token.getToken());
			type.setConsecutive(newConsecutive.getLlaveTabla());
			typeService.update(type);
		} else {
			consecutive = consecutiveService.consultaXId(type.getConsecutive());
		}
		consecutive = consecutiveService.asignarConsecutivo(consecutive, token.getToken());
		_voucher.getHeader().setCode(consecutive.getConsecutivoActual());
		_voucher.getHeader().setCreationDate(new Date());
		_voucher.getHeader()
				.setDeleteDate(Date.from(LocalDate.of(1990, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
		// Desde Angular viene una ultima linea vacia
		for (VoucherLine item : toRemove) {
			_voucher.getRecords().remove(item);
		}
	}

	private CatalogDTO getCatalog(VoucherDTO _voucher) throws ServerException {
		if (_voucher.getCatalog() == null)
			throw new ServerException("Es importante identificar el catalogo para guardar el comprobante");
		if (_voucher.getFactDate() == null)
			throw new ServerException("El comprobante debe tener una fecha");
		CatalogDTO catalogDTO = catalogService.getById(_voucher.getCatalog());
		if (catalogDTO == null)
			throw new ServerException("No se encontro un catalogo con ese identificador");
		_voucher.setCatalogCode(catalogDTO.getCode());
		if (catalogDTO.getInitialDate() != null && _voucher.getFactDate().compareTo(catalogDTO.getInitialDate()) < 0)
			throw new ServerException("La fecha del comprobante debe ser mayor al periodo del catalogo. Fecha inicial "
					+ catalogDTO.getInitialDate().toString());
		if (catalogDTO.getFinalDate() != null && _voucher.getFactDate().compareTo(catalogDTO.getFinalDate()) > 0)
			throw new ServerException("La fecha del comprobante debe ser menor al periodo del catalogo. Fecha final "
					+ catalogDTO.getFinalDate().toString());
		return catalogDTO;
	}

	private VoucherDTO getVoucherById(String catalogCode, String voucherId) throws ServerException {
		// Como no funciona el cosultar por id toca el getOne
		VoucherFilterDTO filterVoucher = new VoucherFilterDTO();
		filterVoucher.setKey(voucherId);
		filterVoucher.setCatalogCode(catalogCode);
		return voucherService.getOne(filterVoucher);
	}

}
