package d3.document.infrastructure;

import java.util.List;
import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.RelacionInternaDTO;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.document.domain.PedidoVentaFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "PedidoVentaMapper")
public interface PedidoVentaMapper extends IBasicMapper<PedidoVentaDTO, PedidoVentaFilterDTO> {

	List<String> obtenerFiltrosPorRelacion(@Param("pFilter") PedidoVentaCaracteristicaFilterDTO pFilter,
			@Param("pFilterTemplate") PedidoVentaFilterDTO pFilterTemplate,
			@Param("pIdsFiltered") List<String> pIdsFiltered);

	List<PedidoVentaDTO> listarPermitidos(@Param("dto") PedidoVentaFilterDTO dto,
			@Param("filtroEstados") List<String> filtroEstados, @Param("campoFiltro") List<String> campoFiltro,
			@Param("valorFiltro") String valorFiltro, @Param("ordenNombre") String ordenNombre,
			@Param("ordenDescendente") String ordenDescendente, @Param("filtroTexto") List<String> filtroTexto,
			@Param("filtroEstadoGeneralesMultiple") List<String> filtroEstadosGeneralesMultiple,
			@Param("filtroPorRelaciones") List<String> filtroPorRelaciones,
			@Param("pStaticRelation") List<RelacionInternaDTO> pStaticRelation);

	List<String> optionsToFilterByField(@Param("usuario") String usuario, @Param("relaciones") List<String> relaciones);

	List<PedidoVentaDTO> listarPermitidosPorCampoFiltro(@Param("dto") PedidoVentaFilterDTO dto,
			@Param("filtroEstados") List<String> filtroEstados, @Param("ordenNombre") String ordenNombre,
			@Param("ordenDescendente") String ordenDescendente, @Param("filtroTexto") List<String> filtroTexto,
			@Param("camposFiltro") List<PropiedadDTO> camposFiltro,
			@Param("filtroEstadoGeneralesMultiple") List<String> filtroEstadosGeneralesMultiple,
			@Param("optionsToFilter") List<String> optionsToFilter,
			@Param("filtroPorRelaciones") List<String> filtroPorRelaciones,
			@Param("pStaticRelation") List<RelacionInternaDTO> pStaticRelation);

	List<PedidoVentaDTO> listarExpedientesDisponiblesDocumento(PedidoVentaFilterDTO dto);

	List<PedidoVentaDTO> listarExpedientesDisponiblesDocumentoFuncion(@Param("dto") PedidoVentaFilterDTO dto,
			@Param("funcionBusqueda") String funcionBusqueda, @Param("filtroEstados") List<String> filtroEstados,
			@Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);

	List<PedidoVentaDTO> listarExpedientesPertenecenCampo(String campo);

	void actualizarEstados(PedidoVentaDTO dto);

	int contarEstados(PedidoVentaDTO dto);

	List<PedidoVentaDTO> listarUsuario(PedidoVentaFilterDTO dto);

	List<PedidoVentaDTO> listar2Ids(@Param("Ids") List<String> Ids);

	// Lo items hijos de los principales que son visibles
	List<PedidoVentaDTO> listarVisibleRenderNivel2(@Param("documentos") List<PedidoVentaDTO> documentos);

	// PAra las decisiones tipo iteracion
	List<PedidoVentaDTO> iteracion(@Param("sqlFuncionDecision") String sqlFuncionDecision,
			@Param("llaveTablaDocumento") String llaveTablaDocumento,
			@Param("llaveTablaModificador") String llaveTablaModificador, @Param("ramdom") String ramdom);

	List<PedidoVentaDTO> getByNameTemplateAndConsecutive(@Param("pName") String pName,
			@Param("pTemplate") String pTemplate, @Param("pConsecutive") String pConsecutive);
}