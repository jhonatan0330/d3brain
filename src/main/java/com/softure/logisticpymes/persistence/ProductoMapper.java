package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.util.List;
import org.apache.ibatis.annotations.Param;
// END region interImport
import com.softure.logisticpymes.dto.ProductoDTO;
import com.softure.logisticpymes.dto.filter.ProductoFilterDTO;

public interface ProductoMapper extends IBasicMapper<ProductoDTO, ProductoFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<ProductoDTO> listarProductoCampo(@Param("campo")String campo, @Param("filtro") String filtro);
	List<ProductoDTO> listarProductoPlantillaResponsable(ProductoFilterDTO dto);
	List<ProductoDTO> listarProductoDisponible(ProductoFilterDTO dto);
	List<ProductoDTO> listarProductoFuncion(@Param("funcion")String funcion, @Param("documento")String documento, @Param("filtro") String filtro,@Param("token") String token);
	List<ProductoDTO> listarProductoSimplificado(@Param("productos") List<ProductoDTO> productos);
// END region aditionalMethods
}