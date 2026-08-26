package d3.authentication.infrastructure;

import d3.D3SqlConnMapper;
import d3.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import d3.authentication.domain.UsuarioAutenticacionAutorizacionFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "UsuarioAutenticacionAutorizacionMapper")
public interface UsuarioAutenticacionAutorizacionMapper
		extends IBasicMapper<UsuarioAutenticacionAutorizacionDTO, UsuarioAutenticacionAutorizacionFilterDTO> {

}