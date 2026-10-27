package d3.configuration.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.configuration.domain.TreeNodeDTO;

@D3SqlConnMapper(value = "NodoHijoMapper")
public interface NodoHijoMapper {

	/**
	 * Devuelve los hijos directos de un nodo en una sola consulta UNION ALL.
	 * tipoPadre: ORGANIZACION | PROCESO | PLANTILLA (ver TreeNodeDTO)
	 * llavePadre: llave del nodo padre
	 * Las columnas de BD no se modifican; solo se renombran las propiedades del DTO.
	 */
	List<TreeNodeDTO> listarHijos(@Param("tipoPadre") String tipoPadre, @Param("llavePadre") String llavePadre);

}
