package com.accounting.plan.application;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.CatalogService;
import com.accounting.plan.application.base.FormatLineService;
import com.accounting.plan.application.base.FormatVoucherService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.FormatLineDTO;
import com.accounting.plan.domain.FormatVoucherDTO;
import com.accounting.plan.domain.ResultMapConst;
import com.accounting.plan.domain.ResultMapDTO;
import com.softure.java.dto.exception.ServerException;

@Service("PlanCreateAccountTemplateAccountingService")
public class PlanCreateAccountService {

	@Autowired
	private AccountService accountService;
	@Autowired
	private FormatVoucherService formatService;
	@Autowired
	private FormatLineService lineService;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private ResultMapExtendService mapService;

	public AccountDTO call(AccountDTO account, String token) throws ServerException {
		return  accountService.save(account, token);
	}
	
	@Transactional(value = "accountingTransactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public AccountDTO configurate(AccountDTO account, String token) throws ServerException {
		if (account.getType() != null && account.getType().compareTo(AccountConst.TYPE_GROUP) != 0
				&& account.getTemplate() != null) {
			FormatVoucherDTO format = new FormatVoucherDTO();
			format.setCatalog(account.getCatalog());
			format.setTemplate(account.getTemplate());
			format = formatService.save(format, token);
			FormatLineDTO line = new FormatLineDTO();
			line.setAccount(account.getKey());
			line.setFormat(format.getKey());
			if (account.getOperation() != null && account.getOperation().compareTo(AccountConst.OPERATION_MINUS) == 0) {
				line.setNegative("1");
			} else {
				line.setPositive("1");
			}
			lineService.save(line, token);
		}
		createMatrix(account, token);
		return account;
	}

	private void createMatrix(AccountDTO account, String token) throws ServerException {
		CatalogDTO catalog = catalogService.getById(account.getCatalog());

		createLevel0(catalog.getInitialDate(), account.getKey(), catalog.getCode(), ResultMapConst.TYPE_PUNTUAL, token);
		createLevel0(catalog.getInitialDate(), account.getKey(), catalog.getCode(), ResultMapConst.TYPE_TEMPORAL,
				token);
		createLevel1(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_PUNTUAL, token);
		createLevel1(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_TEMPORAL, token);
		createLevel2(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_PUNTUAL, token);
		createLevel2(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_TEMPORAL, token);
		createLevel3(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_PUNTUAL, token);
		createLevel3(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_TEMPORAL, token);
		createLevel4(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_PUNTUAL, token);
		createLevel4(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_TEMPORAL, token);
		createLevel5(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_PUNTUAL, token);
		createLevel5(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_TEMPORAL, token);
	}

	private void createLevel0(Date initialDate, String accountId, String catalogCode, String type, String token)
			throws ServerException {
		ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
		map.setLevel(0);
		map.setMapDate(initialDate);
		mapService.save(map, token);
	}

	private void createLevel1(Date initialDate, Date endDate, String accountId, String catalogCode, String type,
			String token) throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(1);
			map.setMapDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			System.out.println("(1)" + date.getTime().toString());
			mapService.save(map, token);
			date.add(Calendar.YEAR, 1);
		}
	}

	private void createLevel2(Date initialDate, Date endDate, String accountId, String catalogCode, String type,
			String token) throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(2);
			map.setMapDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setYear(date.get(Calendar.MONTH));
			System.out.println("(2)" + date.getTime().toString());
			mapService.save(map, token);
			date.add(Calendar.MONTH, 1);
		}
	}

	private void createLevel3(Date initialDate, Date endDate, String accountId, String catalogCode, String type,
			String token) throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(3);
			map.setMapDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			System.out.println("(3)" + date.getTime().toString());
			mapService.save(map, token);
			date.add(Calendar.DATE, 1);
		}
	}

	private void createLevel4(Date initialDate, Date endDate, String accountId, String catalogCode, String type,
			String token) throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(4);
			map.setMapDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setHour(date.get(Calendar.HOUR));
			mapService.save(map, token);
			date.add(Calendar.HOUR, 1);
		}
	}

	private void createLevel5(Date initialDate, Date endDate, String accountId, String catalogCode, String type,
			String token) throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(5);
			map.setMapDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setHour(date.get(Calendar.HOUR));
			map.setMinute(date.get(Calendar.MINUTE));
			mapService.save(map, token);
			date.add(Calendar.MINUTE, 10);
		}
	}

	private ResultMapDTO getBaseMap(String accountId, String catalogCode, String type) {
		ResultMapDTO map = new ResultMapDTO();
		map.setAccount(accountId);
		map.setCatalog(catalogCode);
		map.setType(type);
		return map;
	}

}