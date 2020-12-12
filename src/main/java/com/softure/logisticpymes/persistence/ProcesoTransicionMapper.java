package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;

import org.apache.ibatis.annotations.Param;

// END region interImport
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.filter.ProcesoTransicionFilterDTO;

public interface ProcesoTransicionMapper extends IBasicMapper<ProcesoTransicionDTO, ProcesoTransicionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProcesoTransicionDTO> listarTransicionesRol(ProcesoTransicionFilterDTO dto);
	List<ProcesoTransicionDTO> listarTransaccionInicial(ProcesoTransicionFilterDTO dto);
	String decision(@Param("sqlFuncionDecision") String sqlFuncionDecision, @Param("llaveTablaDocumento") String llaveTablaDocumento, @Param("llaveTablaModificador") String llaveTablaModificador);
// END region aditionalMethods
}