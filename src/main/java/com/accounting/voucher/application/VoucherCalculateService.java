package com.accounting.voucher.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.application.base.ResultMapExtendService;
import com.accounting.plan.application.base.TimeFrameService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.accounting.plan.domain.TimeFrameDTO;
import com.accounting.voucher.application.base.AccountRecordService;
import com.accounting.voucher.domain.AccountRecordDTO;
import com.accounting.voucher.domain.Voucher;
import com.shared.domain.ServerException;
import com.softure.process_form.application.ConsecutivoSvc;

@Service
public class VoucherCalculateService {

	@Autowired
	@Lazy
	private CatalogService catalogService;
	@Autowired
	@Lazy
	private AccountService accountService;
	@Autowired
	@Lazy
	private VoucherGetService voucherService;
	@Autowired
	@Lazy
	private AccountRecordService recordService;
	@Autowired
	@Lazy
	private ConsecutivoSvc consecutiveService;
	@Autowired
	@Lazy
	private ResultMapExtendService mapService;
	@Autowired
	@Lazy
	private TimeFrameService timeFrameService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void call(String voucherId) throws ServerException {
		Voucher voucher = voucherService.getById(voucherId);
		calculateBalance(voucher);
	}

	private void calculateBalance(Voucher _voucher) throws ServerException {
		for (AccountRecordDTO item : _voucher.getRecords()) {
			saveMap(item.getAccount(), item.getFactDate(), item.getPositive(), item.getNegative(), item.getValue());
			// Para mejorar el tema de las dimensiones
			if (item.getThird() != null) {
				saveMap(item.getThird(), item.getFactDate(), item.getPositive(), item.getNegative(), item.getValue());
			}
			if (item.getCenter() != null) {
				saveMap(item.getCenter(), item.getFactDate(), item.getPositive(), item.getNegative(), item.getValue());
			}
		}
	}

	private void saveMap(String accountId, Date factDate, BigDecimal positive, BigDecimal negative, BigDecimal value)
			throws ServerException {
		AccountDTO account = accountService.getById(accountId);
		// Obtener la fila de la cuenta en todos los niveles
		List<ResultMapDTO> mapItems = mapService.getItemsAccount(accountId, factDate);
		// sumarle el valor a cada nivel
		for (ResultMapDTO resultMapDTO : mapItems) {
			if (positive.compareTo(BigDecimal.ZERO) != 0) {
				resultMapDTO.setPositive(resultMapDTO.getPositive().add(positive));
			} else {
				resultMapDTO.setNegative(resultMapDTO.getNegative().add(negative));
			}
			if (account.getOperation().compareTo(AccountConst.OPERATION_MINUS) == 0) {
				resultMapDTO.setValue(resultMapDTO.getValue().add(value.negate()));
				resultMapDTO.setNextBalance(resultMapDTO.getNextBalance().add(value.negate()));
			}else {
				resultMapDTO.setValue(resultMapDTO.getValue().add(value));
				resultMapDTO.setNextBalance(resultMapDTO.getNextBalance().add(value));
			}
			
			resultMapDTO.setQuantity(resultMapDTO.getQuantity() + 1);
			resultMapDTO.setAverage(resultMapDTO.getValue()
					.divide(new BigDecimal(resultMapDTO.getQuantity()), 2, RoundingMode.CEILING).floatValue());
			// guardar las modificaciones
			mapService.update(resultMapDTO);
			TimeFrameDTO timeFrame = timeFrameService.getById(resultMapDTO.getTimeFrame());
			mapService.updateBalance(resultMapDTO.getAccount(), timeFrame.getStartDate(), timeFrame.getLevel(), value);
		}

		if (account.getParent() != null) {
			AccountDTO accountParent = accountService.getById(account.getParent());
			// Las cuentas auxilires de terceros y centros no agrupan no deban acumular
			if (accountParent!=null && accountParent.getType().compareTo(AccountConst.TYPE_GROUP) == 0) {
				saveMap(account.getParent(), factDate, positive, negative, value);
			}
		}
	}

}
