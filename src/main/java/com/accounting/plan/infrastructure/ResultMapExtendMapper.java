package com.accounting.plan.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.accounting.plan.domain.AccountDTO;
import com.accounting.plan.domain.ResultMapDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper("ResultMapExtendAccountingMapper")
public interface ResultMapExtendMapper {

	ResultMapDTO updateItem(@Param("catalogCode") String catalogCode, @Param("item") ResultMapDTO dto);

	ResultMapDTO updateBalance(ResultMapDTO dto);

	List<ResultMapDTO> getBalance(@Param("catalogId") String catalogId, @Param("catalogCode") String catalogCode);

	void insertAll(@Param("catalog") String catalogCode, @Param("type") String type,
			@Param("list") List<ResultMapDTO> dto);

	List<ResultMapDTO> getItemsAccount(@Param("catalogCode") String catalogCode, @Param("accountId") String accountId,
			@Param("type") String type, @Param("year") int year, @Param("month") int month, @Param("day") int day,
			@Param("hour") int hour, @Param("minute") int minute);

	List<AccountDTO> selectAccountExtendTime();

}