package com.softure.process_designer.infrastructure;


// BEGIN region interImport  
import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.SoftureSqlConnMapper;
import com.softure.java.domain.IBasicMapper;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;

@SoftureSqlConnMapper(value = "ProcesoTransicionMapper")
public interface ProcesoTransicionMapper extends IBasicMapper<ProcesoTransicionDTO, ProcesoTransicionFilterDTO>{
	
	List<ProcesoTransicionDTO> listarTransicionesRol(ProcesoTransicionFilterDTO dto);
	List<ProcesoTransicionDTO> listarTransaccionInicial(ProcesoTransicionFilterDTO dto);
	String decision(@Param("sqlFuncionDecision") String sqlFuncionDecision, @Param("llaveTablaDocumento") String llaveTablaDocumento, @Param("llaveTablaModificador") String llaveTablaModificador);
	BigDecimal valorEntransicionParaRevertir(@Param("documento") String documento, @Param("expediente") String expediente);
	List<ProcesoTransicionDTO> getFullToSynchronize(@Param("process") List<String> process);
	int funcionRegresarTablaHistoricos(@Param("documentId") String documentId);
}