package d3.configuration.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.accounting.domain.IndicatorDTO;
import d3.authorization.domain.RolAccesoDTO;
import d3.configuration.domain.NodoArbolDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.RelacionInternaDTO;

@D3SqlConnMapper(value = "NodoHijoMapper")
public interface NodoHijoMapper {

	/**
	 * Devuelve los hijos directos de un nodo en una sola consulta UNION ALL.
	 * categoriaPadre: O | P | L (ver PropiedadValorDefinidoDTO)
	 * llavePadre: llave del nodo padre
	 */
	List<NodoArbolDTO> listarHijos(@Param("tipoPadre") String tipoPadre, @Param("llavePadre") String llavePadre);

	List<PropiedadDTO> listarPropiedadesFull();

	List<RelacionInternaDTO> listarRelacionesFull();

	List<RolAccesoDTO> listarRolesFull();

	NodoArbolDTO obtenerOrganizacionPrincipal();

	List<NodoArbolDTO> listarApisFull(@Param("process") List<String> process);

	List<NodoArbolDTO> listarProcesosFull(@Param("process") List<String> process);

	List<NodoArbolDTO> listarEstadosFull(@Param("process") List<String> process);

	List<NodoArbolDTO> listarTransicionesFull(@Param("process") List<String> process);

	List<NodoArbolDTO> listarPlantillasFull(@Param("process") List<String> process);

	List<NodoArbolDTO> listarReportesFull(@Param("process") List<String> process);

	List<NodoArbolDTO> listarCamposFull(@Param("process") List<String> process);

	List<IndicatorDTO> listarIndicadoresFull(@Param("process") List<String> process);

}
