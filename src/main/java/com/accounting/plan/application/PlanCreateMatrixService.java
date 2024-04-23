package com.accounting.plan.application;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.ResultMapExtendService;
import com.accounting.plan.domain.AccountConst;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.ResultMapConst;
import com.accounting.plan.domain.ResultMapDTO;
import com.shared.domain.ServerException;

@Service("PlanCreateMatrixAccountingService")
public class PlanCreateMatrixService {

	@Autowired
	private ResultMapExtendService mapService;
	@Autowired
	private AccountService accountService;
	
	public void call(CatalogDTO catalog, AccountDTO account) throws ServerException {

		createLevel1(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_PUNTUAL);
		createLevel2(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_PUNTUAL);
		createLevel3(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_PUNTUAL);
		createLevel4(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(), 
				ResultMapConst.TYPE_PUNTUAL);
		createLevel5(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(), 
				ResultMapConst.TYPE_PUNTUAL);
		createLevel1(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_TEMPORAL);
		createLevel2(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_TEMPORAL);
		createLevel3(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(), 
				ResultMapConst.TYPE_TEMPORAL);
		createLevel4(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(),
				ResultMapConst.TYPE_TEMPORAL);
		createLevel5(catalog.getInitialDate(), catalog.getFinalDate(), account.getKey(), catalog.getCode(), 
				ResultMapConst.TYPE_TEMPORAL);
		System.out.println(account.getName());
		account = accountService.getById(account.getKey());
		account.setStatus(AccountConst.STATUS_OPERATING);
		accountService.update(account);
		if(account.getParent()!=null) {
			AccountDTO parent = accountService.getById(account.getParent());
			if(parent.getStatus().compareTo(AccountConst.STATUS_PLANNING)==0)
				call(catalog, parent);
		}
	}


	private void createLevel1(Date initialDate, Date endDate, String accountId, String catalogCode,  String type) throws ServerException {
		List<ResultMapDTO> items = new ArrayList<>();
		ResultMapDTO map0 = getBaseMap(accountId, catalogCode, type);
		map0.setLevel(0);
		map0.setStartDate(initialDate);
		map0.setEndDate(endDate);
		map0.setPeriod("0");
		items.add(map0);
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(1);
			map.setStartDate(date.getTime());
			map.setPeriod(String.valueOf(date.get(Calendar.YEAR)));
			map.setYear(date.get(Calendar.YEAR));
			date.add(Calendar.YEAR, 1);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		mapService.saveAll(catalogCode, type, items);
	}

	private void createLevel2(Date initialDate, Date endDate, String accountId, String catalogCode, String type) throws ServerException {
		List<ResultMapDTO> items = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(2);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setPeriod(String.valueOf(date.get(Calendar.YEAR)) + "-" + String.format("%02d", date.get(Calendar.MONTH)+1));
			date.add(Calendar.MONTH, 1);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		mapService.saveAll(catalogCode, type, items);
	}

	private void createLevel3(Date initialDate, Date endDate, String accountId, String catalogCode, String type) throws ServerException {
		List<ResultMapDTO> items = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(3);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setPeriod(String.valueOf(date.get(Calendar.YEAR)) + "-" + String.format("%02d", date.get(Calendar.MONTH)+1)+ "-" + String.format("%02d", date.get(Calendar.DATE)));
			date.add(Calendar.DATE, 1);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		mapService.saveAll(catalogCode, type, items);
	}

	private void createLevel4(Date initialDate, Date endDate, String accountId, String catalogCode, String type) throws ServerException {
		List<ResultMapDTO> items = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(4);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setHour(date.get(Calendar.HOUR_OF_DAY));
			map.setPeriod(String.valueOf(date.get(Calendar.YEAR)) + "-" + String.format("%02d", date.get(Calendar.MONTH)+1)+ "-" + String.format("%02d", date.get(Calendar.DATE))+ " " + String.format("%02d", date.get(Calendar.HOUR_OF_DAY)) + ":00 - " +String.format("%02d", date.get(Calendar.HOUR_OF_DAY)) + ":59");
			date.add(Calendar.HOUR, 1);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		mapService.saveAll(catalogCode, type, items);
	}

	private void createLevel5(Date initialDate, Date endDate, String accountId, String catalogCode, String type) throws ServerException {
		List<ResultMapDTO> items = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogCode, type);
			map.setLevel(5);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setHour(date.get(Calendar.HOUR_OF_DAY));
			map.setMinute(date.get(Calendar.MINUTE));
			map.setPeriod(String.valueOf(date.get(Calendar.YEAR)) + "-" + String.format("%02d", date.get(Calendar.MONTH)+1)+ "-" + String.format("%02d", date.get(Calendar.DATE))+ " " + String.format("%02d", date.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", date.get(Calendar.MINUTE)) + " - " + String.format("%02d", date.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", date.get(Calendar.MINUTE) + 9));
			date.add(Calendar.MINUTE, 10);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		mapService.saveAll(catalogCode, type, items);
	}

	private ResultMapDTO getBaseMap(String accountId, String catalogCode, String type) {
		ResultMapDTO map = new ResultMapDTO();
		map.setAccount(accountId);
		map.setCatalog(catalogCode);
		map.setType(type);
		return map;
	}
}
