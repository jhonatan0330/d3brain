package d3.users.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.shared.domain.IBasicMapper;
import d3.users.domain.UsuarioDTO;
import d3.users.domain.UsuarioFilterDTO;

@D3SqlConnMapper(value = "UsuarioMapper")
public interface UsuarioMapper extends IBasicMapper<UsuarioDTO, UsuarioFilterDTO> {

	List<UsuarioDTO> listarRol(UsuarioFilterDTO dto);

	List<UsuarioDTO> getUsersState(@Param("document") String document);

	UsuarioDTO getUserByDocument(@Param("pDocument") String pDocument);
}