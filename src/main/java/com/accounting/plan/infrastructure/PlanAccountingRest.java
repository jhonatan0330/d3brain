package com.accounting.plan.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.plan.application.PlanCreateCatalogOrganizationService;
import com.accounting.plan.application.base.IPlanCreateCatalogService;
import com.accounting.plan.domain.CatalogDTO;
import com.softure.java.dto.exception.ServerException;

@RestController
@RequestMapping("acc/plan")
public class PlanAccountingRest {
	
	@Autowired
	private IPlanCreateCatalogService createCatalogService;
	@Autowired
	private PlanCreateCatalogOrganizationService createCatalogOrganizatonService;
	
	@PostMapping("/createCatalog")
	public void createCatalog(@RequestBody CatalogDTO catalog, @RequestHeader("Authorization") String token) throws ServerException {
		createCatalogService.call(catalog, token);
	}
	
	@PostMapping("/test")
	public void test() throws ServerException {
		createCatalogOrganizatonService.call("123");
	}

}
