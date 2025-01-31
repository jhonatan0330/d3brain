package com.accounting.voucher.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.application.base.ResultMapExtendService;
import com.accounting.plan.application.base.StackVoucherService;
import com.accounting.plan.application.base.TimeFrameService;
import com.accounting.plan.application.base.TypeService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.StackVoucherDTO;
import com.accounting.plan.domain.TypeDTO;
import com.accounting.voucher.application.base.AccountRecordService;
import com.accounting.voucher.application.base.VoucherService;
import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.Voucher;
import com.accounting.voucher.domain.VoucherDTO;
import com.accounting.voucher.domain.VoucherFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.shared.domain.SharedIdResponse;
import com.shared.domain.SharedToken;
import com.softure.process_form.application.ConsecutivoSvc;
import com.softure.process_form.domain.ConsecutivoDTO;

@Service
public class VoucherCreateService {

	@Autowired @Lazy 
	private CatalogService catalogService;
	@Autowired @Lazy 
	private AccountService accountService;
	@Autowired @Lazy 
	private VoucherService voucherService;
	@Autowired @Lazy 
	private AccountRecordService recordService;
	@Autowired @Lazy 
	private ConsecutivoSvc consecutiveService;
	@Autowired @Lazy 
	private ResultMapExtendService mapService;
	@Autowired @Lazy 
	private TimeFrameService timeFrameService;
	@Autowired @Lazy 
	private StackVoucherService stackBasicService;
	@Autowired @Lazy 
	private TypeService typeService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(Voucher _voucher, SharedToken token) throws ServerException {
		CatalogDTO catalogDTO = getCatalog(_voucher.getHeader());
		validateInfoHeaderAndRecords(_voucher, token);
		
		configureAccounts(_voucher, catalogDTO);
		voucherService.save(_voucher.getHeader());
		VoucherDTO headerDTO = getVoucherById(catalogDTO.getCode(), _voucher.getHeader().getKey());
		saveRecords(catalogDTO.getCode(), _voucher, headerDTO.getKey());
		stackVoucher(headerDTO.getKey());
		// Esto lo retiro por el momenot miestras esten junto //el codigo al final para
		// evitar errores en transaccionalidad
		// getCodeVoucher(catalogDTO, headerDTO, token.getToken());
		return new SharedIdResponse(headerDTO.getKey(), headerDTO.getCode());
	}

	private void stackVoucher(String voucherId) throws ServerException {
		StackVoucherDTO stack = new StackVoucherDTO();
		stack.setVoucher(voucherId);
		stackBasicService.save(stack);
	}

	private void configureAccounts(Voucher _voucher, CatalogDTO catalogDTO) throws ServerException {
		for (AccountRecordDTO item : _voucher.getRecords()) {
			AccountDTO account = accountService.getById(item.getAccount());
			if (account == null)
				throw new ServerException("La cuenta no existe en la base de datos");
			if (account.getCatalog().compareTo(catalogDTO.getKey()) != 0)
				throw new ServerException("La cuenta no pertenece al catalogo. " + account.getName());
			if (account.getState().compareTo(SharedConstants.STATE_ACTIVE) != 0)
				throw new ServerException("La cuenta no se encuentra activa. " + account.getName());
			createMapLine(catalogDTO, account);
		}
	}

	private void createMapLine(CatalogDTO catalogDTO, AccountDTO account) throws ServerException {
		if (account.getInitialDate() == null || account.getFinalDate() == null) {
			mapService.insertMapAccount(account.getKey(), catalogDTO.getInitialDate(), catalogDTO.getFinalDate());
			account.setInitialDate(catalogDTO.getInitialDate());
			account.setFinalDate(catalogDTO.getFinalDate());
			accountService.update(account);
			if(account.getParent()!=null) createMapLine(catalogDTO, accountService.getById(account.getParent()));	
		}else {
			/*if (account.getInitialDate().compareTo(_voucher.getHeader().getFactDate()) > 0)
				throw new ServerException(
						"La cuenta no ha generado el esquema de valores, la fecha inicial de la cuenta es mayor a la fecha del voucher.");
			if (account.getFinalDate().compareTo(_voucher.getHeader().getFactDate()) < 0)
				throw new ServerException(
						"La cuenta no ha generado el esquema de valores, la fecha final de la cuenta es menor a la fecha del voucher.");*/	
		}
		
	}
	


	private void saveRecords(String catalogCode, Voucher _voucher, String headerId) throws ServerException {
		for (AccountRecordDTO item : _voucher.getRecords()) {
			if (item.getAccount() != null) {
				item.setVoucher(headerId);
				item.setCatalogCode(catalogCode);
				recordService.save(item);
			}
		}
	}

	private void validateInfoHeaderAndRecords(Voucher _voucher, SharedToken token) throws ServerException {
		if (_voucher == null)
			throw new ServerException("Es en serio no enviaste informacion");
		if (_voucher.getHeader() == null)
			throw new ServerException("Te hace falta la informacion de encabezado del comprobante");
		if (_voucher.getRecords() == null || _voucher.getRecords().isEmpty())
			throw new ServerException("Es curiosos pero no enviaste registros de cuentas");
		if (_voucher.getHeader().getValue() == null || _voucher.getHeader().getValue().compareTo(BigDecimal.ZERO) == 0)
			throw new ServerException("El valor total del comprobante no esta diligenciado");
		BigDecimal valueAllRecords = BigDecimal.ZERO;
		List<AccountRecordDTO> toRemove = new ArrayList<>();
		for (AccountRecordDTO recordAccount : _voucher.getRecords()) {
			if (recordAccount.getAccount() != null && recordAccount.getAccount().isEmpty())
				recordAccount.setAccount(null);
			if (recordAccount.getPositive() == null)
				recordAccount.setPositive(BigDecimal.ZERO);
			if (recordAccount.getNegative() == null)
				recordAccount.setNegative(BigDecimal.ZERO);
			recordAccount.setValue(recordAccount.getPositive().add(recordAccount.getNegative().negate()));
			if (recordAccount.getAccount() == null && recordAccount.getValue().compareTo(BigDecimal.ZERO) != 0)
				throw new ServerException("Existe un registro con valor " + recordAccount.getValue()
						+ " pero no tiene una cuenta asignada");
			if (recordAccount.getAccount() != null && recordAccount.getValue().compareTo(BigDecimal.ZERO) == 0)
				throw new ServerException("Existe un registro sin valor pero no tiene una cuenta asignada");
			if (recordAccount.getAccount() == null && recordAccount.getValue().compareTo(BigDecimal.ZERO) == 0) {
				toRemove.add(recordAccount);
			} else {
				valueAllRecords = valueAllRecords.add(recordAccount.getPositive());
				if (recordAccount.getNote() != null && recordAccount.getNote().isEmpty())
					recordAccount.setNote(null);
				recordAccount.setFactDate(_voucher.getHeader().getFactDate());
			}
		}
		
		TypeDTO type = typeService.getById(_voucher.getHeader().getType());
		if (type == null)
			throw new ServerException("No se reconoce el tipo de documento");
		if (type.getAutomatic() && _voucher.getHeader().getDocument() == null)
			throw new ServerException("El tipo de documento es automatico y no se ha enviado el documento");
		
		if (type.getPattern().compareTo(AccountConst.TYPE_PATTERN_COMPROBANTE)==0 && _voucher.getHeader().getValue().compareTo(valueAllRecords) != 0)
			throw new ServerException("El valor total del comprobante (" + _voucher.getHeader().getValue()
					+ ") no concuerda con los valores positivos de los registros (" + valueAllRecords + ")");
		
		ConsecutivoDTO consecutive = null;
		if (type.getConsecutive() == null) {
			ConsecutivoDTO newConsecutive = new ConsecutivoDTO();
			newConsecutive.setNombre(type.getName());
			newConsecutive.setPrefijo(type.getCode() + "-");
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
		
		_voucher.getHeader().setCreatedUser(token.getUser());
		_voucher.getHeader().setCreatedUserName(token.getUserName());
		// Desde Angular viene una ultima linea vacia
		for (AccountRecordDTO item : toRemove) {
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
		if (_voucher.getFactDate().compareTo(catalogDTO.getInitialDate()) < 0)
			throw new ServerException("La fecha del comprobante debe ser mayor al periodo del catalogo. Fecha inicial "
					+ catalogDTO.getInitialDate().toString());
		if (_voucher.getFactDate().compareTo(catalogDTO.getFinalDate()) > 0)
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

	public SharedIdResponse update(Voucher _voucher, SharedToken token) throws ServerException {
		voucherService.update(_voucher.getHeader());
		return new SharedIdResponse(_voucher.getHeader().getKey(), _voucher.getHeader().getCode());
	}
	
	public VoucherDTO delete(String voucherId) throws ServerException{
		return voucherService.delete(voucherId);
	}
}
