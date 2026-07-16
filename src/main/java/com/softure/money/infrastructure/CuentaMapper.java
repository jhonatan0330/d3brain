package com.softure.money.infrastructure;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.money.domain.CuentaDTO;
import com.softure.money.domain.CuentaFilterDTO;

@SoftureSqlConnMapper(value = "CuentaMapper")
public interface CuentaMapper extends IBasicMapper<CuentaDTO, CuentaFilterDTO> {
	Long sobregiro(String documento);

	boolean turnomultiple(String documento);
}