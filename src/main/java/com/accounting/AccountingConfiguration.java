package com.accounting;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.accounting.plan.application.base.ResultMapExtendService;
import com.shared.domain.ServerException;

@Configuration
@EnableScheduling
public class AccountingConfiguration {

	@Autowired
	ResultMapExtendService extendService;
	
	@Scheduled(fixedDelayString = "${fixedDelayAccount.in.milliseconds}")
	public void configureAccount() throws ServerException {
		extendService.configureAccount();
	}
}
