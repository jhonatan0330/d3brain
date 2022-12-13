package com.softure.document_execution.infrastructure;


// BEGIN region interImport  
import java.util.List;

import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.DetallePedidoVentaFilterDTO;
import com.softure.java.domain.IBasicMapper;

public interface DetallePedidoVentaMapper extends IBasicMapper<DetallePedidoVentaDTO, DetallePedidoVentaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DetallePedidoVentaDTO> listar2Documento(String documento);
// END region aditionalMethods
}