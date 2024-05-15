package com.accounting.plan.application.base;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.application.PlanCreateMatrixService;
import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.accounting.plan.infrastructure.ResultMapExtendMapper;
import com.shared.domain.ServerException;

@Service("ResultMapExtendAccountingService")
public class ResultMapExtendService {

	@Autowired
	private ResultMapExtendMapper mapper;
	@Autowired
	private CatalogService catalogService;
	@Autowired
	private PlanCreateMatrixService matrixService;

	public void saveAll(String catalogCode, String type, List<ResultMapDTO> maps) throws ServerException {
		if (maps == null || maps.isEmpty())
			return;
		for (ResultMapDTO resultMapDTO : maps) {
			resultMapDTO.setKey(UUID.randomUUID().toString().replaceAll("-", ""));
		}
		int indexEnd = 0;
		int indexStart = 0;
		while (indexEnd < maps.size()) {
			try {
				indexStart = indexEnd;
				indexEnd = indexEnd + 1000;
				if (indexEnd > maps.size())
					indexEnd = maps.size();
				mapper.insertAll(catalogCode, type, maps.subList(indexStart, indexEnd));

			} catch (BindingException ex) {
				throw new ServerException(ex.getMessage());
			} catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
		}
	}

	public ResultMapDTO update(String catalogCode, ResultMapDTO dto) throws ServerException {
		try {
			return mapper.updateItem(catalogCode, dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public ResultMapDTO updateBalance(ResultMapDTO dto) throws ServerException {
		try {
			return mapper.updateBalance(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<ResultMapDTO> getBalanceByCatalog(String catalogId) throws ServerException {
		CatalogDTO catalog = getCatalog(catalogId);
		try {
			return mapper.getBalance(catalog.getKey(), catalog.getCode());
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	private CatalogDTO getCatalog(String catalogId) throws ServerException {
		if (catalogId == null)
			throw new ServerException("Es necesario colcoar el Id del catalogo");
		CatalogDTO catalog = catalogService.getById(catalogId);
		if (catalog == null)
			throw new ServerException("No se identifico un catalogo con el identificador " + catalogId);
		return catalog;
	}

	public List<ResultMapDTO> getItemsAccount(String catalogCode, String accountId, String type, Date dateFact)
			throws ServerException {
		if (catalogCode == null)
			throw new ServerException("Es necesario colcoar el Id del catalogo");
		Calendar dateFactCalendar = Calendar.getInstance();
		dateFactCalendar.setTime(dateFact);
		try {
			return mapper.getItemsAccount(catalogCode, accountId, type, dateFactCalendar.get(Calendar.YEAR),
					dateFactCalendar.get(Calendar.MONTH), dateFactCalendar.get(Calendar.DATE),
					dateFactCalendar.get(Calendar.HOUR_OF_DAY), (dateFactCalendar.get(Calendar.MINUTE) / 10 * 10));
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<AccountDTO> getAccountWithTimeToExtend() throws ServerException {
		try {
			return mapper.selectAccountExtendTime();
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void configureAccount() throws ServerException {
		List<AccountDTO> accounts = getAccountWithTimeToExtend();
		if (accounts != null && !accounts.isEmpty()) {
			HashMap<String, CatalogDTO> hmap = new HashMap<String, CatalogDTO>();

			for (AccountDTO accountDTO : accounts) {
				if (!hmap.containsKey(accountDTO.getCatalog()))
					hmap.put(accountDTO.getCatalog(), getCatalog(accountDTO.getCatalog()));
				CatalogDTO catalog = hmap.get(accountDTO.getCatalog());
				if (accountDTO.getInitialDate() == null) {
					matrixService.call(accountDTO, catalog.getInitialDate());
				} else if (accountDTO.getFinalDate() == null) {
					throw new ServerException("Uy no deberia pasar esto en una cuenta que tenga fecha de inicio pero no de fin");
				} else if (accountDTO.getInitialDate().compareTo(catalog.getInitialDate()) > 0) {
					Calendar calculateDate = new GregorianCalendar();
					calculateDate.setTime(accountDTO.getInitialDate());
					calculateDate.add(Calendar.DAY_OF_MONTH, -1);
					matrixService.call(accountDTO, calculateDate.getTime());
				} else if (accountDTO.getFinalDate().compareTo(catalog.getFinalDate()) < 0) {
					Calendar calculateDate = new GregorianCalendar();
					calculateDate.setTime(accountDTO.getFinalDate());
					calculateDate.add(Calendar.DAY_OF_MONTH, 1);
					matrixService.call(accountDTO, calculateDate.getTime());
				}
			}
		}
	}

}