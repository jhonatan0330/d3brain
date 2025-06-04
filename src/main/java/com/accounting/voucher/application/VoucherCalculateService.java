package com.accounting.voucher.application;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.PlanGetAccountService;
import com.accounting.plan.application.base.ResultMapExtendService;
import com.accounting.plan.application.base.ResultMapService;
import com.accounting.plan.application.base.TimeFrameService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.accounting.plan.domain.TimeFrameDTO;
import com.accounting.voucher.application.base.AccountRecordService;
import com.accounting.voucher.domain.AccountRecordAuxiliarDTO;
import com.accounting.voucher.domain.Voucher;
import com.accounting.voucher.domain.VoucherLine;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.process_form.application.ConsecutivoSvc;

@Service
public class VoucherCalculateService {


	@Autowired
	@Lazy
	private PlanGetAccountService accountService;
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
	@Autowired
	@Lazy
	private ResultMapService resultMapService;

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void call(String voucherId, String action) throws ServerException {
		Voucher _voucher = voucherService.getById(voucherId);
		BigDecimal _positive =null;
		BigDecimal _negative =null;
		for (VoucherLine item : _voucher.getRecords()) {
			_positive = item.getLine().getPositive();
			_negative = item.getLine().getNegative();
			if(action !=null && action.compareTo(SharedConstants.STATE_INACTIVE)==0) {
				_negative = item.getLine().getPositive();
				_positive = item.getLine().getNegative();
			}
			saveMap(item.getLine().getAccount(), item.getLine().getFactDate(), _positive , _negative, item.getLine().getValue());
			if(item.getReferences()!=null && !item.getReferences().isEmpty()) {
				for (AccountRecordAuxiliarDTO iAux : item.getReferences()) {
					if(iAux.getAccount()!= null)
					saveMap(iAux.getAccount(), item.getLine().getFactDate(), _positive , _negative, item.getLine().getValue());	
				}
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
			if(resultMapDTO.getKey() == null) 
				resultMapDTO = createMapLine( account, resultMapDTO.getTimeFrame());
			
			if (positive.compareTo(BigDecimal.ZERO) != 0) {
				resultMapDTO.setPositive(resultMapDTO.getPositive().add(positive));
			} else {
				resultMapDTO.setNegative(resultMapDTO.getNegative().add(negative));
			}
			if (account.getOperation().compareTo(AccountConst.OPERATION_MINUS) == 0) {
				resultMapDTO.setValue(resultMapDTO.getValue().add(value.negate()));
				resultMapDTO.setNextBalance(resultMapDTO.getNextBalance().add(value));
			}else {
				resultMapDTO.setValue(resultMapDTO.getValue().add(value));
				resultMapDTO.setNextBalance(resultMapDTO.getNextBalance().add(value));
			}
			resultMapDTO.setQuantity(resultMapDTO.getQuantity() + 1);
			// guardar las modificaciones
			mapService.update(resultMapDTO);
			TimeFrameDTO timeFrame = timeFrameService.getById(resultMapDTO.getTimeFrame());
			mapService.updateBalance(resultMapDTO.getAccount(), timeFrame.getStartDate(), timeFrame.getLevel(), value);
		}
		
		if (account.getParent() != null) {
			AccountDTO accountParent = accountService.getById(account.getParent());
			// Las cuentas auxilires de terceros y centros no agrupan no deban acumular
			if(account.getType().compareTo(AccountConst.TYPE_AUXILIAR) == 0) {
				if(accountParent!=null && accountParent.getParent()!=null) {
					AccountDTO accountParentTop = accountService.getById(accountParent.getParent());
					if (accountParentTop!= null && accountParentTop.getParent() != null) {
						AccountDTO _accountGroupParent = accountService.findAccountByDocumentId(account.getCatalog(), account.getDocument(), accountParentTop.getKey());
						
						if (_accountGroupParent != null) {
							saveMap(_accountGroupParent.getKey(), factDate, positive, negative, value);
						}
					}
				}
			} else {
				if (accountParent.getType().compareTo(AccountConst.TYPE_GROUP) == 0) {
					saveMap(account.getParent(), factDate, positive, negative, value);
				} 
			}
		}
		
	}

	
	private ResultMapDTO createMapLine(AccountDTO pAccount, String pTimeFrame) throws ServerException {
		
		ResultMapDTO _map = new ResultMapDTO();
		_map.setAccount(pAccount.getKey());
		_map.setTimeFrame(pTimeFrame);
		BigDecimal _value = mapService.getPreviousBalance(pAccount.getKey(), pTimeFrame);
		if(_value != null) {
			_map.setLastBalance(_value);
			_map.setNextBalance(_value);
		} 
		resultMapService.save(_map);
		return resultMapService.getById(_map.getKey());
	}
}
