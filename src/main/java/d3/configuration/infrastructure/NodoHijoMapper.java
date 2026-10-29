package d3.configuration.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.authentication.domain.OrganizacionDTO;
import d3.authorization.domain.RolAccesoDTO;
import d3.configuration.domain.PropiedadDTO;
import d3.configuration.domain.PropiedadValorDefinidoDTO;
import d3.configuration.domain.RelacionInternaDTO;
import d3.configuration.domain.TreeNodeDTO;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.DocumentoPlantillaDTO;
import d3.process.domain.ProcesoDTO;
import d3.process.domain.ProcesoEstadoDTO;
import d3.process.domain.ProcesoTransicionDTO;
import d3.report.domain.ReporteBaseDTO;
import d3.webservice.domain.WebServiceDTO;

@D3SqlConnMapper(value = "NodoHijoMapper")
public interface NodoHijoMapper {

	/**
	 * Devuelve los hijos directos de un nodo en una sola consulta UNION ALL.
	 * tipoPadre: ORGANIZACION | PROCESO | PLANTILLA (ver TreeNodeDTO)
	 * llavePadre: llave del nodo padre
	 * Las columnas de BD no se modifican; solo se renombran las propiedades del DTO.
	 */
	List<TreeNodeDTO> listarHijos(@Param("tipoPadre") String tipoPadre, @Param("llavePadre") String llavePadre);

	List<PropiedadValorDefinidoDTO> listarTiposPropiedadFull();

	List<PropiedadDTO> listarPropiedadesFull();

	List<RelacionInternaDTO> listarRelacionesFull();

	List<RolAccesoDTO> listarRolesFull();

	OrganizacionDTO obtenerOrganizacionPrincipal();

	List<WebServiceDTO> listarApisFull(@Param("process") List<String> process);

	List<ProcesoDTO> listarProcesosFull(@Param("process") List<String> process);

	List<ProcesoEstadoDTO> listarEstadosFull(@Param("process") List<String> process);

	List<ProcesoTransicionDTO> listarTransicionesFull(@Param("process") List<String> process);

	List<DocumentoPlantillaDTO> listarPlantillasFull(@Param("process") List<String> process);

	List<ReporteBaseDTO> listarReportesFull(@Param("process") List<String> process);

	List<DocumentoPlantillaCaracteristicaDTO> listarCamposFull(@Param("process") List<String> process);

}
