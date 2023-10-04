package com.accounting.plan.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.plan.application.CatalogoSvc;
import com.softure.java.dto.exception.ServerException;

@RestController
@RequestMapping("accounting/plan")
public class PlanRest {

	@Autowired CatalogoSvc service;
	
	@GetMapping("/ping")
	public String ping() throws ServerException {
		service.consultaXId("");
		return "PING";
	}

}
