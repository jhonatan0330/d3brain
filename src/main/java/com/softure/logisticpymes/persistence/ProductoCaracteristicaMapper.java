package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.softure.logisticpymes.dto.ProductoDTO;
// END region interImport
import com.softure.logisticpymes.dto.ProductoCaracteristicaDTO;
import com.softure.logisticpymes.dto.filter.ProductoCaracteristicaFilterDTO;

public interface ProductoCaracteristicaMapper extends IBasicMapper<ProductoCaracteristicaDTO, ProductoCaracteristicaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProductoCaracteristicaDTO> listarProductoSimplificado(@Param("productos") List<ProductoDTO> productos);
// END region aditionalMethods
}