package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;
// END region interImport
import com.softure.logisticpymes.dto.DetallePedidoVentaDTO;
import com.softure.logisticpymes.dto.filter.DetallePedidoVentaFilterDTO;

public interface DetallePedidoVentaMapper extends IBasicMapper<DetallePedidoVentaDTO, DetallePedidoVentaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<DetallePedidoVentaDTO> listar2Documento(String documento);
// END region aditionalMethods
}