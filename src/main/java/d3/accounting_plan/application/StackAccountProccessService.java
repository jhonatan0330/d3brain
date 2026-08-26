package d3.accounting_plan.application;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.accounting_plan.domain.StackVoucherDTO;
import d3.accounting_plan.infrastructure.StackVoucherExtendMapper;
import d3.accounting_plan.infrastructure.StackVoucherMapper;
import d3.accounting_voucher.application.VoucherCalculateService;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;

@Service("StackAccountProccessService")
public class StackAccountProccessService {

	private final StackVoucherExtendMapper stackMapper;
	private final StackVoucherMapper stackBasicMapper;
	private final VoucherCalculateService calculateService;

	public StackAccountProccessService(@Lazy StackVoucherExtendMapper stackMapper,
			@Lazy StackVoucherMapper stackBasicMapper, @Lazy VoucherCalculateService calculateService) {
		this.stackMapper = stackMapper;
		this.stackBasicMapper = stackBasicMapper;
		this.calculateService = calculateService;
	}

	public String call() throws ServerException {
		List<StackVoucherDTO> stack = stackMapper.stackAvailable();
		if (stack == null || stack.size() <= 0)
			return "0";
		for (StackVoucherDTO stackVoucherDTO : stack) {
			stackVoucherDTO.setState(SharedConstants.STATE_COMPLETE);
			stackVoucherDTO.setExecutionDate(new Date());
			stackBasicMapper.update(stackVoucherDTO);
		}
		for (StackVoucherDTO stackVoucherDTO : stack) {
			calculateService.call(stackVoucherDTO.getVoucher(), stackVoucherDTO.getAction());
			stackVoucherDTO.setFinishDate(new Date());
			stackBasicMapper.update(stackVoucherDTO);
		}
		return String.valueOf(stack.size());
	}

}
