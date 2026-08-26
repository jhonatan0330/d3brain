package d3.accounting_plan.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting_plan.domain.StackVoucherDTO;
import d3.accounting_plan.domain.StackVoucherFilterDTO;

@D3SqlConnMapper(value = "StackVoucherAccountingMapper")
public interface StackVoucherMapper {

	StackVoucherDTO insert(StackVoucherDTO dto);

	StackVoucherDTO update(StackVoucherDTO dto);

	int count(StackVoucherFilterDTO filter);

	StackVoucherDTO getOne(StackVoucherFilterDTO filter);

	List<StackVoucherDTO> getMany(StackVoucherFilterDTO filter);

}