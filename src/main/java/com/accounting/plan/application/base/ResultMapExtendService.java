package com.accounting.plan.application.base;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.accounting.plan.domain.CatalogDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.accounting.plan.infrastructure.ResultMapExtendMapper;
import com.softure.java.dto.exception.ServerException;

@Service("ResultMapExtendAccountingService")
public class ResultMapExtendService {

	@Autowired
	private ResultMapExtendMapper mapper;
	@Autowired
	private CatalogService catalogService;

	

	public void saveAll(String catalogCode, String type, List<ResultMapDTO> maps) throws ServerException {
		if(maps==null || maps.isEmpty()) return;
		for (ResultMapDTO resultMapDTO : maps) {
			resultMapDTO.setKey(UUID.randomUUID().toString().replaceAll("-", ""));	
		}
		int indexEnd = 0;
		int indexStart = 0;
		while(indexEnd < maps.size()) {
			try {
				indexStart = indexEnd;
				indexEnd = indexEnd + 1000;
				if(indexEnd > maps.size()) indexEnd = maps.size();
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
		if(catalogId ==null) throw new ServerException("Es necesario colcoar el Id del catalogo");
		CatalogDTO catalog = catalogService.getById(catalogId);
		if(catalog ==null) throw new ServerException("No se identifico un catalogo con el identificador " + catalogId);
		try {
			return mapper.getBalance(catalog.getKey(), catalog.getCode());
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public List<ResultMapDTO> getItemsAccount(String catalogCode, String accountId, String type, Date dateFact) throws ServerException {
		if(catalogCode ==null) throw new ServerException("Es necesario colcoar el Id del catalogo");
		Calendar dateFactCalendar = Calendar.getInstance();
		dateFactCalendar.setTime(dateFact);
		try {
			return mapper.getItemsAccount(catalogCode, accountId, type, dateFactCalendar.get(Calendar.YEAR), dateFactCalendar.get(Calendar.MONTH), dateFactCalendar.get(Calendar.DATE), dateFactCalendar.get(Calendar.HOUR_OF_DAY), (dateFactCalendar.get(Calendar.MINUTE)/10*10));
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

}