package com.accounting.plan.application.base;

import com.accounting.plan.domain.CatalogDTO;
import com.softure.java.dto.exception.ServerException;

public interface IPlanCreateCatalogService {

	CatalogDTO call(CatalogDTO catalog, String token) throws ServerException;

}
