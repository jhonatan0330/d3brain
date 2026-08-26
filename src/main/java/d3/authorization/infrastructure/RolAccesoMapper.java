package d3.authorization.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.authorization.domain.RolAccesoDTO;
import d3.authorization.domain.RolAccesoFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "RolAccesoMapper")
public interface RolAccesoMapper extends IBasicMapper<RolAccesoDTO, RolAccesoFilterDTO> {

	List<RolAccesoDTO> consultaUsuarioDocumento(@Param("userId") String userId);

	List<RolAccesoDTO> getFullToSynchronize(@Param("process") List<String> process);

}