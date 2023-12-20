package com.accounting.plan.infrastructure;

import org.apache.ibatis.annotations.Param;

import com.accounting.AccountingSqlConnMapper;

@AccountingSqlConnMapper("CreateCatalogoTablesAccountingMapper")
public interface CreateCatalogTablesMapper {

	void createTemporal(@Param("code") String code);
	
	void createPuntual(@Param("code") String code);
	
	void createVoucher(@Param("code") String code);
	
	void createRegister(@Param("code") String code);
	
	void createAuxiliar(@Param("code") String code);
	
}