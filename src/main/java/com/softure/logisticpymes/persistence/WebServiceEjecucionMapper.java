package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;

// END region interImport
import com.softure.logisticpymes.dto.WebServiceEjecucionDTO;
import com.softure.logisticpymes.dto.filter.WebServiceEjecucionFilterDTO;

public interface WebServiceEjecucionMapper extends IBasicMapper<WebServiceEjecucionDTO, WebServiceEjecucionFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<WebServiceEjecucionDTO> apisTransaccion();
	int hasPropertiesAsync();
// END region aditionalMethods
}