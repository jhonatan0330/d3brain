package d3.accounting_plan.application.base;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.ibatis.binding.BindingException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.accounting_plan.domain.ResultMapDTO;
import d3.accounting_plan.domain.TimeFrameDTO;
import d3.accounting_plan.infrastructure.ResultMapExtendMapper;
import d3.shared.domain.ServerException;

@Service("ResultMapExtendAccountingService")
public class ResultMapExtendService {

	private final ResultMapExtendMapper mapper;

	public ResultMapExtendService(@Lazy ResultMapExtendMapper mapper) {
		this.mapper = mapper;
	}

	public void saveAll(List<TimeFrameDTO> maps) throws ServerException {
		if (maps == null || maps.isEmpty())
			return;
		for (TimeFrameDTO resultMapDTO : maps) {
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
				mapper.insertAll(maps.subList(indexStart, indexEnd));

			} catch (BindingException ex) {
				throw new ServerException(ex.getMessage());
			} catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
		}
	}

	public ResultMapDTO update(ResultMapDTO dto) throws ServerException {
		try {
			return mapper.updateItem(dto);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public BigDecimal getPreviousBalance(String accountId, String timeFrameId) throws ServerException {
		try {
			return mapper.getPreviousBalance(accountId, timeFrameId);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public ResultMapDTO updateBalance(String accountId, Date startDate, int level, BigDecimal value)
			throws ServerException {
		try {
			return mapper.updateBalance(accountId, startDate, level, value);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<ResultMapDTO> getBalanceByCatalog(String catalogId) throws ServerException {
		// CatalogDTO catalog = getCatalog(catalogId);
		try {
			return mapper.getBalance(catalogId);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<ResultMapDTO> getItemsAccount(String accountId, Date dateFact) throws ServerException {
		Calendar dateFactCalendar = Calendar.getInstance();
		dateFactCalendar.setTime(dateFact);
		try {
			return mapper.getItemsAccount(accountId, dateFactCalendar.get(Calendar.YEAR),
					dateFactCalendar.get(Calendar.MONTH), dateFactCalendar.get(Calendar.DATE),
					dateFactCalendar.get(Calendar.HOUR_OF_DAY), (dateFactCalendar.get(Calendar.MINUTE) / 10 * 10));
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public TimeFrameDTO getTimeFrameLevel(int level) throws ServerException {
		try {
			return mapper.selectTimeFrameLevel(level);
		} catch (BindingException ex) {
			throw new ServerException(ex.getMessage());
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

}