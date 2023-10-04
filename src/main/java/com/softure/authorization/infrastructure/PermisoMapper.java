package com.softure.authorization.infrastructure;


import com.softure.SoftureSqlConnMapper;
import com.softure.authorization.domain.PermisoDTO;
import com.softure.authorization.domain.PermisoFilterDTO;
import com.softure.java.domain.IBasicMapper;

@SoftureSqlConnMapper("PermisoMapper")
public interface PermisoMapper extends IBasicMapper<PermisoDTO, PermisoFilterDTO>{
	

// BEGIN region aditionalMethods  
// END region aditionalMethods
}