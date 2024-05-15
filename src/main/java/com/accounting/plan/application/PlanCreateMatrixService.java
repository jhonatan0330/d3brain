package com.accounting.plan.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.application.base.AccountService;
import com.accounting.plan.application.base.ResultMapExtendService;
import com.accounting.plan.application.base.ResultMapService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.ResultMapConst;
import com.accounting.plan.domain.ResultMapDTO;
import com.accounting.plan.domain.ResultMapFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;

@Service("PlanCreateMatrixAccountingService")
public class PlanCreateMatrixService {

	@Autowired
	private ResultMapExtendService mapService;
	@Autowired
	private ResultMapService resultMapService;
	@Autowired
	private AccountService accountService;

	public void call(AccountDTO account, Date initialDate) throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		Calendar finalDate = new GregorianCalendar();
		finalDate.setTime(initialDate);
		finalDate.add(Calendar.DAY_OF_MONTH, 1);
		createLevel0(date.getTime(), finalDate.getTime(), account.getKey(), account.getCatalog(),
				ResultMapConst.TYPE_PUNTUAL);
		createLevel1(date.getTime(), finalDate.getTime(), account.getKey(), account.getCatalog(),
				ResultMapConst.TYPE_PUNTUAL);
		createLevel2(date.getTime(), finalDate.getTime(), account.getKey(), account.getCatalog(),
				ResultMapConst.TYPE_PUNTUAL);
		createLevel3(date.getTime(), finalDate.getTime(), account.getKey(), account.getCatalog(),
				ResultMapConst.TYPE_PUNTUAL);
		createLevel0(date.getTime(), finalDate.getTime(), account.getKey(), account.getCatalog(),
				ResultMapConst.TYPE_TEMPORAL);
		createLevel1(date.getTime(), finalDate.getTime(), account.getKey(), account.getCatalog(),
				ResultMapConst.TYPE_TEMPORAL);
		createLevel2(date.getTime(), finalDate.getTime(), account.getKey(), account.getCatalog(),
				ResultMapConst.TYPE_TEMPORAL);
		createLevel3(date.getTime(), finalDate.getTime(), account.getKey(), account.getCatalog(),
				ResultMapConst.TYPE_TEMPORAL);
		System.out.println(date.getTime().toString() + " -  " + account.getName());
		account = accountService.getById(account.getKey());
		if (account.getInitialDate() == null) {
			account.setInitialDate(date.getTime());
		} else {
			if (date.getTime().compareTo(account.getInitialDate()) < 0)
				account.setInitialDate(date.getTime());
		}
		if (account.getFinalDate() == null) {
			account.setFinalDate(date.getTime());
		} else {
			if (date.getTime().compareTo(account.getFinalDate()) > 0)
				account.setFinalDate(date.getTime());
		}
		accountService.update(account);
	}

	private void createLevel0(Date initialDate, Date endDate, String accountId, String catalogId, String type)
			throws ServerException {
		ResultMapDTO mapCurrent = getCurrentMap(accountId, 0, null, null, null, type);
		if (mapCurrent != null) {
			if (initialDate.compareTo(mapCurrent.getStartDate()) < 0) {
				mapCurrent.setStartDate(initialDate);
				resultMapService.update(mapCurrent);
			}
			if (endDate.compareTo(mapCurrent.getEndDate()) > 0) {
				mapCurrent.setEndDate(endDate);
				resultMapService.update(mapCurrent);
			}
			return;
		}
		ResultMapDTO map0 = getBaseMap(accountId, catalogId, type, null);
		map0.setLevel(0);
		map0.setStartDate(initialDate);
		map0.setEndDate(endDate);
		map0.setPeriod("0");
		resultMapService.save(map0);
	}

	private void createLevel1(Date initialDate, Date endDate, String accountId, String catalogId, String type)
			throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		ResultMapDTO mapCurrent = getCurrentMap(accountId, 1, date.get(Calendar.YEAR), null, null, type);
		if (mapCurrent != null) {
			if (initialDate.compareTo(mapCurrent.getStartDate()) < 0) {
				mapCurrent.setStartDate(initialDate);
				resultMapService.update(mapCurrent);
			}
			if (endDate.compareTo(mapCurrent.getEndDate()) > 0) {
				mapCurrent.setEndDate(endDate);
				resultMapService.update(mapCurrent);
			}
			return;
		}
		Calendar previousDate = Calendar.getInstance();
		previousDate.setTime(initialDate);
		previousDate.add(Calendar.YEAR, -1);
		mapCurrent = getCurrentMap(accountId, 1, previousDate.get(Calendar.YEAR), null, null, type);
		ResultMapDTO map = getBaseMap(accountId, catalogId, type, mapCurrent);
		map.setLevel(1);
		map.setStartDate(initialDate);
		map.setPeriod(String.valueOf(date.get(Calendar.YEAR)));
		map.setYear(date.get(Calendar.YEAR));
		
		date.add(Calendar.YEAR, 1);
		date.set(Calendar.MONTH, 0);
		date.set(Calendar.DAY_OF_MONTH, 1);
		map.setEndDate(date.getTime());
		resultMapService.save(map);
	}

	private void createLevel2(Date initialDate, Date endDate, String accountId, String catalogId, String type)
			throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		ResultMapDTO mapCurrent = getCurrentMap(accountId, 2, date.get(Calendar.YEAR), date.get(Calendar.MONTH), null,
				type);
		if (mapCurrent != null) {
			if (initialDate.compareTo(mapCurrent.getStartDate()) < 0) {
				mapCurrent.setStartDate(initialDate);
				resultMapService.update(mapCurrent);
			}
			if (endDate.compareTo(mapCurrent.getEndDate()) > 0) {
				mapCurrent.setEndDate(endDate);
				resultMapService.update(mapCurrent);
			}
			return;
		}
		Calendar previousDate = Calendar.getInstance();
		previousDate.setTime(initialDate);
		previousDate.add(Calendar.MONTH, -1);
		mapCurrent = getCurrentMap(accountId, 2, previousDate.get(Calendar.YEAR), previousDate.get(Calendar.MONTH),
				null, type);
		ResultMapDTO map = getBaseMap(accountId, catalogId, type, mapCurrent);
		map.setLevel(2);
		map.setStartDate(initialDate);
		map.setPeriod(String.valueOf(date.get(Calendar.YEAR)));
		map.setYear(date.get(Calendar.YEAR));
		map.setMonth(date.get(Calendar.MONTH));
		map.setPeriod(
				String.valueOf(date.get(Calendar.YEAR)) + "-" + String.format("%02d", date.get(Calendar.MONTH) + 1));
		date.add(Calendar.MONTH, 1);
		date.set(Calendar.DAY_OF_MONTH, 1);
		map.setEndDate(date.getTime());
		resultMapService.save(map);
	}

	private void createLevel3(Date initialDate, Date endDate, String accountId, String catalogId, String type)
			throws ServerException {
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		ResultMapDTO mapCurrent = getCurrentMap(accountId, 3, date.get(Calendar.YEAR), date.get(Calendar.MONTH),
				date.get(Calendar.DATE), type);
		if (mapCurrent != null) {
			if (initialDate.compareTo(mapCurrent.getStartDate()) < 0)
				mapCurrent.setStartDate(initialDate);
			if (endDate.compareTo(mapCurrent.getEndDate()) > 0)
				mapCurrent.setEndDate(endDate);
			return;
		}
		List<ResultMapDTO> items = new ArrayList<>();

		Calendar previousDate = Calendar.getInstance();
		previousDate.setTime(initialDate);
		previousDate.add(Calendar.DATE, -1);
		mapCurrent = getCurrentMap(accountId, 3, previousDate.get(Calendar.YEAR), previousDate.get(Calendar.MONTH),
				previousDate.get(Calendar.DATE), type);
		ResultMapDTO map = getBaseMap(accountId, catalogId, type, mapCurrent);
		map.setLevel(3);
		map.setStartDate(date.getTime());
		map.setYear(date.get(Calendar.YEAR));
		map.setMonth(date.get(Calendar.MONTH));
		map.setDay(date.get(Calendar.DATE));
		map.setPeriod(
				String.valueOf(date.get(Calendar.YEAR)) + "-" + String.format("%02d", date.get(Calendar.MONTH) + 1)
						+ "-" + String.format("%02d", date.get(Calendar.DATE)));
		date.add(Calendar.DATE, 1);
		map.setEndDate(date.getTime());
		items.add(map);

		items.addAll(createLevel4(initialDate, endDate, accountId, catalogId, type));
		items.addAll(createLevel5(initialDate, endDate, accountId, catalogId, type));
		mapService.saveAll(catalogId, type, items);
	}

	private ResultMapDTO getCurrentMap(String accountId, Integer level, Integer year, Integer mont, Integer day,
			String type) throws ServerException {
		return getCurrentMap(accountId, level, year, mont, day, null, null, type);
	}

	private ResultMapDTO getCurrentMap(String accountId, Integer level, Integer year, Integer mont, Integer day,
			Integer hour, Integer minute, String type) throws ServerException {
		ResultMapFilterDTO filter = new ResultMapFilterDTO();
		filter.setAccount(accountId);
		filter.setLevel(level);
		filter.setType(type);
		filter.setYear(year);
		filter.setMonth(mont);
		filter.setDay(day);
		filter.setHour(hour);
		filter.setMinute(minute);
		filter.setState(SharedConstants.STATE_ACTIVE);
		return resultMapService.getOne(filter);
	}

	private List<ResultMapDTO> createLevel4(Date initialDate, Date endDate, String accountId, String catalogId,
			String type) throws ServerException {
		List<ResultMapDTO> items = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		Calendar previousDate = Calendar.getInstance();
		previousDate.setTime(initialDate);
		previousDate.add(Calendar.HOUR_OF_DAY, -1);
		ResultMapDTO mapCurrent = getCurrentMap(accountId, 4, previousDate.get(Calendar.YEAR),
				previousDate.get(Calendar.MONTH), previousDate.get(Calendar.DATE),
				previousDate.get(Calendar.HOUR_OF_DAY), null, type);

		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogId, type, mapCurrent);
			map.setLevel(4);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setHour(date.get(Calendar.HOUR_OF_DAY));
			map.setPeriod(
					String.valueOf(date.get(Calendar.YEAR)) + "-" + String.format("%02d", date.get(Calendar.MONTH) + 1)
							+ "-" + String.format("%02d", date.get(Calendar.DATE)) + " "
							+ String.format("%02d", date.get(Calendar.HOUR_OF_DAY)) + ":00 - "
							+ String.format("%02d", date.get(Calendar.HOUR_OF_DAY)) + ":59");
			date.add(Calendar.HOUR, 1);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		return items;
	}

	private List<ResultMapDTO> createLevel5(Date initialDate, Date endDate, String accountId, String catalogId,
			String type) throws ServerException {
		List<ResultMapDTO> items = new ArrayList<>();
		Calendar date = Calendar.getInstance();
		date.setTime(initialDate);
		Calendar previousDate = Calendar.getInstance();
		previousDate.setTime(initialDate);
		previousDate.add(Calendar.MINUTE, -10);
		ResultMapDTO mapCurrent = getCurrentMap(accountId, 5, previousDate.get(Calendar.YEAR),
				previousDate.get(Calendar.MONTH), previousDate.get(Calendar.DATE), previousDate.get(Calendar.HOUR_OF_DAY), previousDate.get(Calendar.MINUTE),  type);
		while (date.getTime().compareTo(endDate) < 0) {
			ResultMapDTO map = getBaseMap(accountId, catalogId, type, mapCurrent);
			map.setLevel(5);
			map.setStartDate(date.getTime());
			map.setYear(date.get(Calendar.YEAR));
			map.setMonth(date.get(Calendar.MONTH));
			map.setDay(date.get(Calendar.DATE));
			map.setHour(date.get(Calendar.HOUR_OF_DAY));
			map.setMinute(date.get(Calendar.MINUTE));
			map.setPeriod(
					String.valueOf(date.get(Calendar.YEAR)) + "-" + String.format("%02d", date.get(Calendar.MONTH) + 1)
							+ "-" + String.format("%02d", date.get(Calendar.DATE)) + " "
							+ String.format("%02d", date.get(Calendar.HOUR_OF_DAY)) + ":"
							+ String.format("%02d", date.get(Calendar.MINUTE)) + " - "
							+ String.format("%02d", date.get(Calendar.HOUR_OF_DAY)) + ":"
							+ String.format("%02d", date.get(Calendar.MINUTE) + 9));
			date.add(Calendar.MINUTE, 10);
			map.setEndDate(date.getTime());
			items.add(map);
		}
		return items;
	}

	private ResultMapDTO getBaseMap(String accountId, String catalogId, String type, ResultMapDTO mapCurrent) {
		ResultMapDTO map = new ResultMapDTO();
		map.setAccount(accountId);
		map.setCatalog(catalogId);
		map.setType(type);
		if (mapCurrent != null) {
			map.setNextBalance(mapCurrent.getNextBalance());
			map.setLastBalance(mapCurrent.getLastBalance());
		} else {
			// Si no le coloco los ceros va a fallar el insert all
			map.setNextBalance(BigDecimal.ZERO);
			map.setLastBalance(BigDecimal.ZERO);
		}
		return map;
	}

}
