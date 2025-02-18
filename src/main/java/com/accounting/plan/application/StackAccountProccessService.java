package com.accounting.plan.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.accounting.plan.domain.StackVoucherDTO;
import com.accounting.plan.infrastructure.StackVoucherExtendMapper;
import com.accounting.plan.infrastructure.StackVoucherMapper;
import com.accounting.voucher.application.VoucherCalculateService;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;

@Service("StackAccountProccessService")
public class StackAccountProccessService {

	@Lazy @Autowired
	private StackVoucherExtendMapper stackMapper;
	@Lazy @Autowired
	private StackVoucherMapper stackBasicMapper;
	@Lazy @Autowired
	private VoucherCalculateService calculateService;

	public String call() throws ServerException {
		List<StackVoucherDTO> stack = stackMapper.stackAvailable();
		if (stack == null || stack.size() <= 0)
			return "0";
		for (StackVoucherDTO stackVoucherDTO : stack) {
			calculateService.call(stackVoucherDTO.getVoucher());
			stackVoucherDTO.setState(SharedConstants.STATE_COMPLETE);
			stackBasicMapper.update(stackVoucherDTO);
		}
		return String.valueOf(stack.size());
	}

}
