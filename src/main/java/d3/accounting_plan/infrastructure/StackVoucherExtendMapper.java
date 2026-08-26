package d3.accounting_plan.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.accounting_plan.domain.StackVoucherDTO;

@D3SqlConnMapper(value = "StackVoucherExtendAccountingMapper")
public interface StackVoucherExtendMapper {

	List<StackVoucherDTO> stackAvailable();

}