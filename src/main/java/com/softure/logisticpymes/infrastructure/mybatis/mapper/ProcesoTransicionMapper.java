package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.domain.filter.ProcesoTransicionFilterDTO;

public interface ProcesoTransicionMapper extends IBasicMapper<ProcesoTransicionDTO, ProcesoTransicionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProcesoTransicionDTO> listarTransicionesRol(ProcesoTransicionFilterDTO dto);
	List<ProcesoTransicionDTO> listarTransaccionInicial(ProcesoTransicionFilterDTO dto);
	String decision(@Param("sqlFuncionDecision") String sqlFuncionDecision, @Param("llaveTablaDocumento") String llaveTablaDocumento, @Param("llaveTablaModificador") String llaveTablaModificador);
	BigDecimal valorEntransicionParaRevertir(@Param("documento") String documento, @Param("expediente") String expediente);
// END region aditionalMethods
}