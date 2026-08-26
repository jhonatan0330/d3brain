package d3.accounting_plan.infrastructure;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.accounting_plan.domain.ResultMapDTO;
import d3.accounting_plan.domain.TimeFrameDTO;

@D3SqlConnMapper(value = "ResultMapExtendAccountingMapper")
public interface ResultMapExtendMapper {

	ResultMapDTO updateItem(@Param("item") ResultMapDTO dto);

	ResultMapDTO updateBalance(@Param("accountId") String accountId, @Param("startDate") Date startDate,
			@Param("level") int level, @Param("value") BigDecimal value);

	void insertMapAccount(@Param("accountId") String accountId, @Param("startDateYear") Date startDateYear,
			@Param("endDateYear") Date endDateYear, @Param("startDateMonth") Date startDateMont,
			@Param("endDateMonth") Date endDateMonth, @Param("startDateDay") Date startDateDay,
			@Param("endDateDay") Date endDateDay);

	List<ResultMapDTO> getBalance(@Param("catalogId") String catalogId);

	void insertAll(@Param("list") List<TimeFrameDTO> dto);

	List<ResultMapDTO> getItemsAccount(@Param("accountId") String accountId, @Param("year") int year,
			@Param("month") int month, @Param("day") int day, @Param("hour") int hour, @Param("minute") int minute);

	TimeFrameDTO selectTimeFrameLevel(@Param("level") int level);

	BigDecimal getPreviousBalance(@Param("accountId") String accountId, @Param("timeFrameId") String timeFrameId);

}