package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.ProductoDTO;
import com.softure.logisticpymes.domain.dto.PropiedadDTO;
import com.softure.logisticpymes.domain.filter.PropiedadFilterDTO;

public interface PropiedadMapper extends IBasicMapper<PropiedadDTO, PropiedadFilterDTO>{
	

// BEGIN region aditionalMethods  
	String funcionAsignacion(@Param("estado") String estado, @Param("documento") String documento, @Param("modificador") String modificador, @Param("token") String token);
	void funcionPrevalidacionPlantilla(@Param("funcion") String funcion,@Param("campos") List<PedidoVentaCaracteristicaDTO> campos);
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
// END region aditionalMethods
}