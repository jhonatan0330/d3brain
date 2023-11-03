package com.accounting.plan.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accounting.plan.application.PlanCreateCatalogOrganizationService;
import com.accounting.plan.application.PlanGetCatalogService;
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
	@Autowired
	private PlanGetCatalogService getService;
	
	@PostMapping("/catalog")
	public CatalogDTO createCatalog(@RequestBody CatalogDTO catalog, @RequestHeader("Authorization") String token) throws ServerException {
		return createCatalogService.call(catalog, token);
	}
	
	@GetMapping("/catalog")
	public List<CatalogDTO> getCatalog(@RequestHeader("Authorization") String token) throws ServerException {
		return getService.getActive();
	}
	
	@GetMapping(value="/catalog/{id}")
	public CatalogDTO getById(@PathVariable String id, @RequestHeader("Authorization") String token)  throws ServerException  {
		return getService.getById(id);
	}
	
	@PostMapping("/test")
	public void test() throws ServerException {
		createCatalogOrganizatonService.call("123");
	}

}
