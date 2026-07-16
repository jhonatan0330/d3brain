package com.softure.process_designer.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;

@SoftureSqlConnMapper(value = "ProcesoMapper")
public interface ProcesoMapper extends IBasicMapper<ProcesoDTO, ProcesoFilterDTO> {

	List<ProcesoDTO> noUsoSoloQuitoErrorList();

	List<ProcesoDTO> getFullToSynchronize(@Param("process") List<String> process);
}