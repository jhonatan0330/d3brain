package com.accounting.plan.infrastructure;

import java.util.List;

import com.accounting.plan.domain.StackVoucherDTO;
import com.accounting.plan.domain.StackVoucherFilterDTO;
import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "StackVoucherAccountingMapper")
public interface StackVoucherMapper {

	StackVoucherDTO insert(StackVoucherDTO dto);

	StackVoucherDTO update(StackVoucherDTO dto);

	int count(StackVoucherFilterDTO filter);

	StackVoucherDTO getOne(StackVoucherFilterDTO filter);

	List<StackVoucherDTO> getMany(StackVoucherFilterDTO filter);

}