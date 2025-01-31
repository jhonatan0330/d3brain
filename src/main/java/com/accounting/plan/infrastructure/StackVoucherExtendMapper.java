package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.plan.domain.StackVoucherDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "StackVoucherExtendAccountingMapper")
public interface StackVoucherExtendMapper {

	List<StackVoucherDTO> stackAvailable();

}