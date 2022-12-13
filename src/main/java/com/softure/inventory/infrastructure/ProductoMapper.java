package com.softure.inventory.infrastructure;


// BEGIN region interImport  
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.softure.inventory.domain.ProductoDTO;
import com.softure.inventory.domain.ProductoFilterDTO;
import com.softure.java.domain.IBasicMapper;

public interface ProductoMapper extends IBasicMapper<ProductoDTO, ProductoFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProductoDTO> listarProductoCampo(@Param("campo")String campo, @Param("filtro") String filtro);
	List<ProductoDTO> listarProductoPlantillaResponsable(ProductoFilterDTO dto);
	List<ProductoDTO> listarProductoDisponible(ProductoFilterDTO dto);
	List<ProductoDTO> listarProductoFuncion(@Param("funcion")String funcion, @Param("documento")String documento, @Param("filtro") String filtro,@Param("token") String token);
	List<ProductoDTO> listarProductoSimplificado(@Param("productos") List<ProductoDTO> productos);
	ProductoDTO filtrarPorCodigo(@Param("codigo") String codigo); 
// END region aditionalMethods
}