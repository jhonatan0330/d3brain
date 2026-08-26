package d3.process_designer.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.process_designer.domain.ProcesoEstadoDTO;
import d3.process_designer.domain.ProcesoEstadoFilterDTO;

@D3SqlConnMapper(value = "ProcesoEstadoMapper")
public interface ProcesoEstadoMapper extends IBasicMapper<ProcesoEstadoDTO, ProcesoEstadoFilterDTO> {

	void actualizarEstados(ProcesoEstadoDTO dto);

	List<ProcesoEstadoDTO> actualizarCierreResponsable(ProcesoEstadoDTO dto);

	List<ProcesoEstadoDTO> actualizarIngresoResponsable(ProcesoEstadoDTO dto);

	String funcionAsignacion(@Param("estado") String estado, @Param("documento") String documento,
			@Param("modificador") String modificador, @Param("token") String token);

	List<ProcesoEstadoDTO> getFullToSynchronize(@Param("process") List<String> process);
}