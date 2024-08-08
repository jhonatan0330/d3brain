package com.accounting.plan.infrastructure;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;

@SoftureSqlConnMapper(value = "CreateCatalogoTablesAccountingMapper")
public interface CreateCatalogTablesMapper {

	void createTemporal(@Param("code") String code);
	
	void createPuntual(@Param("code") String code);
	
	void createVoucher(@Param("code") String code);
	
	void createRegister(@Param("code") String code);
	
	void createAuxiliar(@Param("code") String code);
	
}