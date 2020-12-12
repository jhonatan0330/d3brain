package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;
// END region interImport
import com.softure.logisticpymes.dto.ReporteBaseDTO;
import com.softure.logisticpymes.dto.filter.ReporteBaseFilterDTO;

public interface ReporteBaseMapper extends IBasicMapper<ReporteBaseDTO, ReporteBaseFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ReporteBaseDTO> listarMenu();
// END region aditionalMethods
}