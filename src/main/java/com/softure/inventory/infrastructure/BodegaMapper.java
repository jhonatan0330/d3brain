package com.softure.inventory.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.inventory.domain.BodegaDTO;
import com.softure.inventory.domain.BodegaFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper(value = "BodegaMapper")
public interface BodegaMapper extends IBasicMapper<BodegaDTO, BodegaFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}