package com.softure.property.infrastructure;


// BEGIN region interImport  
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.java.domain.IBasicMapper;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadFilterDTO;

public interface PropiedadMapper extends IBasicMapper<PropiedadDTO, PropiedadFilterDTO>{
	

// BEGIN region aditionalMethods  
	String funcionAsignacion(@Param("estado") String estado, @Param("documento") String documento, @Param("modificador") String modificador, @Param("token") String token);
	void funcionPrevalidacionPlantilla(@Param("funcion") String funcion,@Param("campos") List<PedidoVentaCaracteristicaDTO> campos);
	void funcionPrevalidateAPI(@Param("funcion") String funcion,@Param("documento") String documento, @Param("modificador") String modificador, @Param("extracciones") String extracciones);
	void crearFuncion(PropiedadDTO dto);
	void crearFuncionMail(PropiedadDTO dto);
	void crearFuncionFiltros(PropiedadDTO dto);
	void crearFuncionProductos(PropiedadDTO dto);
	void crearFuncionDecision(PropiedadDTO dto);
	void crearFuncionIteracion(PropiedadDTO dto);
	void crearFuncionTarifas(PropiedadDTO dto);
	void crearFuncionNumerica(PropiedadDTO dto);
	void crearFuncionParametros(PropiedadDTO dto);
	void crearFuncionCampoGenerar(PropiedadDTO dto);
	void crearFuncionCamposEspecialesPlantilla(PropiedadDTO dto);
	void crearFuncionPrevalidacion(PropiedadDTO dto);
	void crearFuncionPrevalidateAPI(PropiedadDTO dto);
	void eliminarFuncionPrevalidateAPI(PropiedadDTO dto);
	void eliminarFuncionPrevalidacion(PropiedadDTO dto);
	void eliminarFuncionCamposEspecialesPlantilla(PropiedadDTO dto);
	void eliminarFuncionCampoGenerar(PropiedadDTO dto);
	void eliminarFuncionNumerica(PropiedadDTO dto);
	void eliminarFuncionTarifas(PropiedadDTO dto);
	void eliminarFuncionDecision(PropiedadDTO dto);
	void eliminarFuncionIteracion(PropiedadDTO dto);
	void eliminarFuncion(PropiedadDTO dto);
	void eliminarFuncionFiltros(PropiedadDTO dto);
	void eliminarFuncionProductos(PropiedadDTO dto);
	void actualizarValorPropiedad(PropiedadDTO dto);
	List<PropiedadDTO> consultarRol(@Param("dto")PropiedadFilterDTO dto, @Param("usuario")String usuario, @Param("fecha") Date fecha);
	List<PropiedadDTO> consultarPermisosFullPlantilla(PropiedadDTO dto);
	List<PropiedadDTO> consultarTemporizadoresPendientes();
	List<PropiedadDTO> listarProductoSimplificado(@Param("productos") List<ProductoDTO> productos);
	List<PropiedadDTO> obtenerEspecialFullPermisosSimplificandoBD(@Param("plantillas") List<DocumentoPlantillaDTO> plantillas);
	List<PropiedadDTO> listarPlantillasSimplificar(@Param("plantillas") List<DocumentoPlantillaDTO> plantillas, @Param("usuario")String usuario, @Param("fecha") Date fecha);
	List<PropiedadDTO> getFullPropertiesToConfiguration();
// END region aditionalMethods
}