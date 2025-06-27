package com.accounting.voucher.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		Map<String, ResultMapDTO> acumulador = new HashMap<String, ResultMapDTO>();
		for (VoucherLine item : _voucher.getRecords()) {

			saveMap(item.getLine().getAccount(), item.getLine().getFactDate(), item.getLine().getPositive() , item.getLine().getNegative(), item.getLine().getValue(), acumulador, action);
			if(item.getReferences()!=null && !item.getReferences().isEmpty()) {
				for (AccountRecordAuxiliarDTO iAux : item.getReferences()) {
					if(iAux.getAccount()!= null)
					saveMap(iAux.getAccount(), item.getLine().getFactDate(), item.getLine().getPositive() , item.getLine().getNegative(), item.getLine().getValue(), acumulador, action);	
				}
			}
		}
		// guardar las modificaciones
		Map<String, TimeFrameDTO> _times = new HashMap<String, TimeFrameDTO>();
		for (ResultMapDTO cpv : acumulador.values()) {
			if(cpv.getKey() == null) {
				resultMapService.save(cpv);
			} else {
				mapService.update(cpv);	
			}
			TimeFrameDTO timeFrame = null;
			if (_times.containsKey(cpv.getTimeFrame())) {
				timeFrame = _times.get(cpv.getTimeFrame());
		    } else {
		    	timeFrame = timeFrameService.getById(cpv.getTimeFrame());
		        _times.put(cpv.getTimeFrame(), timeFrame);
		    }
			if (timeFrame != null)			
				mapService.updateBalance(cpv.getAccount(), timeFrame.getStartDate(), timeFrame.getLevel(), cpv.getValueInProcessing());
		}
	}

	private void saveMap(String accountId, Date factDate, BigDecimal positive, BigDecimal negative, BigDecimal value, Map<String, ResultMapDTO> acumulador, String action)
			throws ServerException {
		AccountDTO account = accountService.getById(accountId);

		// Obtener la fila de la cuenta en todos los niveles
		List<ResultMapDTO> mapItems = new ArrayList<ResultMapDTO>();
		for (Map.Entry<String, ResultMapDTO> entry : acumulador.entrySet()) {
		    String clave = entry.getKey();
		    if(clave.startsWith(accountId+ "|")) {
		    	mapItems.add(entry.getValue());
		    }
		}
		if(mapItems.isEmpty()) {
			mapItems = mapService.getItemsAccount(accountId, factDate);
		}

		//el factor es la estrategia para anular
		BigDecimal factor = BigDecimal.ONE;
		if(action !=null && action.compareTo(SharedConstants.STATE_INACTIVE)==0) factor = BigDecimal.ONE.negate();
				
		// sumarle el valor a cada nivel
		for (ResultMapDTO resultMapDTO : mapItems) {
			// Si no existe la fila, crearla, si tiene valor se creo en la misma transacción
			if(resultMapDTO.getKey() == null && resultMapDTO.getValue() == null) 
				resultMapDTO = createMapLine( account, resultMapDTO.getTimeFrame());
			
			if (positive.compareTo(BigDecimal.ZERO) != 0) {
				resultMapDTO.setPositive(resultMapDTO.getPositive().add(positive.multiply(factor)));
			} else {
				resultMapDTO.setNegative(resultMapDTO.getNegative().add(negative.multiply(factor)));
			}
			BigDecimal _valueToApply = value.multiply(factor);
			if (account.getOperation().compareTo(AccountConst.OPERATION_MINUS) == 0) _valueToApply = _valueToApply.negate(); 
			
			resultMapDTO.setValue(resultMapDTO.getValue().add(_valueToApply));
			resultMapDTO.setNextBalance(resultMapDTO.getNextBalance().add(_valueToApply));
			
			if(resultMapDTO.getValueInProcessing() == null)
				resultMapDTO.setValueInProcessing(BigDecimal.ZERO);
			resultMapDTO.setValueInProcessing(resultMapDTO.getValueInProcessing().add(_valueToApply));
			
			resultMapDTO.setQuantity(resultMapDTO.getQuantity() + (1*factor.intValue()));
			
			acumulador.put(accountId + "|" + resultMapDTO.getTimeFrame(), resultMapDTO);
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
							saveMap(_accountGroupParent.getKey(), factDate, positive, negative, value, acumulador, action);
						}
					}
				}
			} else {
				if (accountParent.getType().compareTo(AccountConst.TYPE_GROUP) == 0) {
					saveMap(account.getParent(), factDate, positive, negative, value, acumulador, action);
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
		} else {
			_map.setLastBalance(BigDecimal.ZERO);
			_map.setNextBalance(BigDecimal.ZERO);
		}
		_map.setPositive(BigDecimal.ZERO);
		_map.setNegative(BigDecimal.ZERO);
		_map.setValue(BigDecimal.ZERO);
		_map.setValueInProcessing(BigDecimal.ZERO);
		_map.setQuantity(0);
		return _map;
		//resultMapService.save(_map);
		//return resultMapService.getById(_map.getKey());
	}
}
