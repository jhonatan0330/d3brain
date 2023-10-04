package com.softure.authorization.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.authorization.domain.ModuloDTO;
import com.softure.authorization.domain.ModuloFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper("ModuloMapper")
public interface ModuloMapper extends IBasicMapper<ModuloDTO, ModuloFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}