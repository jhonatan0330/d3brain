package d3.logisticpymes.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.java.domain.IBasicMapper;
import d3.logisticpymes.domain.UsuarioDTO;
import d3.logisticpymes.domain.UsuarioFilterDTO;

@D3SqlConnMapper(value = "UsuarioMapper")
public interface UsuarioMapper extends IBasicMapper<UsuarioDTO, UsuarioFilterDTO> {

	List<UsuarioDTO> listarRol(UsuarioFilterDTO dto);

	List<UsuarioDTO> getUsersState(@Param("document") String document);

	UsuarioDTO getUserByDocument(@Param("pDocument") String pDocument);
}