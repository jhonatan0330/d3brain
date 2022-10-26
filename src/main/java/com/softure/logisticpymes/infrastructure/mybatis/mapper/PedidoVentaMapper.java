package com.softure.logisticpymes.infrastructure.mybatis.mapper;


// BEGIN region interImport  
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.softure.java.domain.IBasicMapper;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaFilterDTO;

public interface PedidoVentaMapper extends IBasicMapper<PedidoVentaDTO, PedidoVentaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PedidoVentaDTO> listarPermitidos(
			@Param("dto")PedidoVentaFilterDTO dto, 
			@Param("filtroEstados") List<String> filtroEstados,
			@Param("campoFiltro") List<String> campoFiltro, 
			@Param("valorFiltro") String valorFiltro, 
			@Param("ordenNombre") String ordenNombre,
			@Param("ordenDescendente") String ordenDescendente,
			@Param("filtroTexto") List<String> filtroTexto);
	List<PedidoVentaDTO> listarPermitidosPorCampoFiltro(
			@Param("dto")PedidoVentaFilterDTO dto, 
			@Param("filtroEstados") List<String> filtroEstados,
			@Param("ordenNombre") String ordenNombre,
			@Param("ordenDescendente") String ordenDescendente,
			@Param("filtroTexto") List<String> filtroTexto,
			@Param("usuario") String usuario,
			@Param("campoFiltro") String campoFiltro);
	List<PedidoVentaDTO> listarExpedientesDisponiblesDocumento(PedidoVentaFilterDTO dto);
	List<PedidoVentaDTO> listarExpedientesDisponiblesDocumentoFuncion(
			@Param("dto")PedidoVentaFilterDTO dto, 
			@Param("funcionBusqueda") String funcionBusqueda, 
			@Param("filtroEstados") List<String> filtroEstados, 
			@Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);
	List<PedidoVentaDTO> listarExpedientesPertenecenCampo(String campo);
	void actualizarEstados(PedidoVentaDTO dto);
	int contarEstados(PedidoVentaDTO dto);
	List<PedidoVentaDTO> listarUsuario(PedidoVentaFilterDTO dto);
	List<PedidoVentaDTO> listar2Ids(@Param("Ids") List<String> Ids);
	List<PedidoVentaDTO> listarVisibleRenderNivel2(@Param("documentos")List<PedidoVentaDTO> documentos);//Lo items hijos de los principales que son visibles
	List<PedidoVentaDTO> iteracion(@Param("sqlFuncionDecision") String sqlFuncionDecision, @Param("llaveTablaDocumento") String llaveTablaDocumento, @Param("llaveTablaModificador") String llaveTablaModificador);//PAra las decisiones tipo iteracion
	PedidoVentaDTO consultarEnVariasPlantillas(@Param("dto")PedidoVentaFilterDTO dto, 
			@Param("filtroPlantillas") List<String> filtroPlantillas);
// END region aditionalMethods
}