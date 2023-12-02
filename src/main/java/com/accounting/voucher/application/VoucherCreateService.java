package com.accounting.voucher.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.PlanCreateMatrixService;
import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.application.base.ResultMapExtendService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.ResultMapConst;
import com.accounting.plan.domain.ResultMapDTO;
import com.accounting.voucher.application.base.AccountRecordService;
import com.accounting.voucher.application.base.VoucherService;
import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.Voucher;
import com.accounting.voucher.domain.VoucherDTO;
import com.accounting.voucher.domain.VoucherFilterDTO;
import com.shared.application.SharedValidateTokenService;
import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.process_form.application.ConsecutivoSvc;
import com.softure.process_form.domain.ConsecutivoDTO;

@Service
public class VoucherCreateService {

	@Autowired
	private CatalogService catalogService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private VoucherService voucherService;
	@Autowired
	private AccountRecordService recordService;
	@Autowired
	private SharedValidateTokenService tokenService;
	@Autowired
	private ConsecutivoSvc consecutiveService;
	@Autowired
	private PlanCreateMatrixService matrixService;
	@Autowired
	private ResultMapExtendService mapService;

	@Transactional(value = "accountingTransactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public SharedIdResponse call(Voucher _voucher, String token) throws ServerException {
		validateInfoHeaderAndRecords(_voucher, token);
		CatalogDTO catalogDTO = getCatalog(_voucher.getHeader());
		configureAccounts(_voucher, catalogDTO, token);
		voucherService.save(_voucher.getHeader(), token);
		VoucherDTO headerDTO = getVoucherById(catalogDTO.getCode(), _voucher.getHeader().getKey());
		saveRecords(catalogDTO.getCode(), _voucher, headerDTO.getKey(), token);
		calculateBalance(catalogDTO, _voucher, headerDTO, token);
		//el codigo al final para evitar errores en transaccionalidad
		getCodeVoucher(catalogDTO, headerDTO, token);
		return new SharedIdResponse(headerDTO.getKey(), headerDTO.getCode());
	}

	private void calculateBalance(CatalogDTO catalogDTO, Voucher _voucher, VoucherDTO headerDTO, String token) throws ServerException {
		for (AccountRecordDTO item : _voucher.getRecords()) {
			saveMap(catalogDTO, item.getAccount(), item.getFactDate(), item.getPositive(), item.getNegative(), item.getValue());
		}
	}

	private void saveMap(CatalogDTO catalogDTO, String accountId, Date factDate, BigDecimal positive, BigDecimal negative, BigDecimal value) throws ServerException {
		// Obtener la fila de la cuenta en todos los niveles
		List<ResultMapDTO> mapItems = mapService.getItemsAccount(catalogDTO.getCode(), accountId, ResultMapConst.TYPE_PUNTUAL, factDate);
		mapItems.addAll( mapService.getItemsAccount(catalogDTO.getCode(), accountId, ResultMapConst.TYPE_TEMPORAL, factDate));
		// sumarle el valor a cada nivel
		for (ResultMapDTO resultMapDTO : mapItems) {
			if(positive.compareTo(BigDecimal.ZERO)!=0) {
				resultMapDTO.setPositive(resultMapDTO.getPositive().add(positive));
			} else {
				resultMapDTO.setNegative(resultMapDTO.getPositive().add(negative));
			}
			resultMapDTO.setValue(resultMapDTO.getValue().add(value));
			resultMapDTO.setQuantity(resultMapDTO.getQuantity()+1);
			resultMapDTO.setAverage(resultMapDTO.getValue().divide(new BigDecimal(resultMapDTO.getQuantity()), 2, RoundingMode.CEILING).floatValue());
			resultMapDTO.setLastBalance(value);
			// guardar las modificaciones
			mapService.update(catalogDTO.getCode(), resultMapDTO);
			mapService.updateBalance(resultMapDTO);
		}
		
		AccountDTO account = accountService.getById( accountId) ;
		if (account.getParent()!=null ) saveMap(catalogDTO, account.getParent(), factDate, positive, negative, value);
	}

	private void configureAccounts(Voucher _voucher, CatalogDTO catalogDTO, String token) throws ServerException {
		for (AccountRecordDTO item : _voucher.getRecords()) {
			AccountDTO account = accountService.getById(item.getAccount());
			if(account == null) throw new ServerException("La cuenta no existe en la base de datos");
			if(account.getCatalog().compareTo(catalogDTO.getKey()) != 0) throw new ServerException("La cuenta no pertenece al catalogo. " + account.getName());
			if(account.getState().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("La cuenta no se encuentra activa. " + account.getName());
			if(account.getStatus().compareTo(AccountConst.STATUS_BLOCKED)==0) throw new ServerException("La cuenta se encuentra bloqueada. " + account.getName());
			if(account.getStatus().compareTo(AccountConst.STATUS_PLANNING)==0)
				matrixService.call(catalogDTO, account, token);
		}
	}
	
	private void saveRecords(String catalogCode, Voucher _voucher, String headerId, String token) throws ServerException {
		for (AccountRecordDTO item : _voucher.getRecords()) {
			if(item.getAccount()!=null) {
				item.setVoucher(headerId);
				item.setCatalogCode(catalogCode);
				recordService.save(item, token);
			}
		}
	}

	private void validateInfoHeaderAndRecords(Voucher _voucher, String token) throws ServerException {
		if (_voucher == null)
			throw new ServerException("Es en serio no enviaste informacion");
		if (_voucher.getHeader() == null)
			throw new ServerException("Te hace falta la informacion de encabezado del comprobante");
		if (_voucher.getRecords() == null || _voucher.getRecords().isEmpty())
			throw new ServerException("Es curiosos pero no enviaste registros de cuentas");
		if (_voucher.getHeader().getValue()==null || _voucher.getHeader().getValue().compareTo(BigDecimal.ZERO)==0)
			throw new ServerException("El valor total del comprobante no esta diligenciado");
		BigDecimal valueAllRecords = BigDecimal.ZERO;
		BigDecimal positiveValueHeader = BigDecimal.ZERO;
		BigDecimal negativeValueHeader = BigDecimal.ZERO;
		List<AccountRecordDTO> toRemove = new ArrayList<>();
		for (AccountRecordDTO recordAccount : _voucher.getRecords()) {
			if(recordAccount.getAccount()!=null && recordAccount.getAccount().isEmpty()) recordAccount.setAccount(null);
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
				if(recordAccount.getNote()!=null && recordAccount.getNote().isEmpty()) recordAccount.setNote(null);
				recordAccount.setFactDate(_voucher.getHeader().getFactDate());
				positiveValueHeader.add(recordAccount.getPositive());
				negativeValueHeader.add(recordAccount.getNegative());
			}
		}
		if (_voucher.getHeader().getValue().compareTo(valueAllRecords) != 0)
			throw new ServerException(
					"El valor total del comprobante ("+_voucher.getHeader().getValue() +") no concuerda con los valores positivos de los registros (" +valueAllRecords + ")");
		_voucher.getHeader().setRegisterDate(new Date());
		_voucher.getHeader().setRegisterUser(tokenService.getUserFlex(token));
		_voucher.getHeader().setPositive(positiveValueHeader);
		_voucher.getHeader().setNegative(negativeValueHeader);
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
			throw new ServerException("La fecha del comprobane debe ser mayor al periodo del catalogo. Fecha inicial "
					+ catalogDTO.getInitialDate().toString());
		if (_voucher.getFactDate().compareTo(catalogDTO.getFinalDate()) > 0)
			throw new ServerException("La fecha del comprobane debe ser menor al periodo del catalogo. Fecha final "
					+ catalogDTO.getFinalDate().toString());
		return catalogDTO;
	}

	private void getCodeVoucher(CatalogDTO catalogDTO, VoucherDTO voucher, String token) throws ServerException {
		ConsecutivoDTO consecutive = null;
		if (catalogDTO.getConsecutive() == null) {
			ConsecutivoDTO newConsecutive = new ConsecutivoDTO();
			newConsecutive.setNombre(catalogDTO.getName());
			newConsecutive.setPrefijo(catalogDTO.getCode() + "-");
			newConsecutive.setNumeroInicial(new BigDecimal(1000));
			newConsecutive.setNumeroActual(new BigDecimal(1000));
			consecutive = consecutiveService.guardar(newConsecutive, token);
			catalogDTO.setConsecutive(newConsecutive.getLlaveTabla());
			catalogService.update(catalogDTO, token);
		} else {
			consecutive = new ConsecutivoDTO();
			consecutive.setLlaveTabla(catalogDTO.getConsecutive());
		}
		consecutive = consecutiveService.asignarConsecutivo(consecutive, token);
		
		voucher = getVoucherById(catalogDTO.getCode(), voucher.getKey());
		voucher.setCatalogCode(catalogDTO.getCode());
		voucher.setCode(consecutive.getConsecutivoActual());
		voucherService.update(voucher, token);
	}

	private VoucherDTO getVoucherById(String catalogCode, String voucherId) throws ServerException {
		//Como no funciona el cnosultar por id toca el getOne
		VoucherFilterDTO filterVoucher = new VoucherFilterDTO();
		filterVoucher.setKey(voucherId);
		filterVoucher.setCatalogCode(catalogCode);
		return voucherService.getOne(filterVoucher);
	}

}
