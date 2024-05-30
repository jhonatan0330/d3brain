package com.accounting.plan.infrastructure;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.accounting.plan.domain.TimeFrameDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper("ResultMapExtendAccountingMapper")
public interface ResultMapExtendMapper {

	ResultMapDTO updateItem(@Param("item") ResultMapDTO dto);

	ResultMapDTO updateBalance(@Param("accountId") String accountId, @Param("startDate") Date startDate, @Param("level") int level, @Param("value")BigDecimal value);

	void insertMapAccount(@Param("accountId") String accountId, @Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	List<ResultMapDTO> getBalance(@Param("catalogId") String catalogId);

	void insertAll(@Param("list") List<TimeFrameDTO> dto);
	
	List<ResultMapDTO> getItemsAccount(@Param("accountId") String accountId,
			@Param("year") int year, @Param("month") int month, @Param("day") int day,
			@Param("hour") int hour, @Param("minute") int minute);

	List<AccountDTO> selectAccountExtendTime();
	
	TimeFrameDTO selectTimeFrameLevel(@Param("level") int level);

}