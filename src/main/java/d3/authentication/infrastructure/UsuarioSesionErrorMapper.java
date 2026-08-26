package d3.authentication.infrastructure;

import d3.D3SqlConnMapper;
import d3.authentication.domain.UsuarioSesionErrorDTO;
import d3.authentication.domain.UsuarioSesionErrorFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "UsuarioSesionErrorMapper")
public interface UsuarioSesionErrorMapper extends IBasicMapper<UsuarioSesionErrorDTO, UsuarioSesionErrorFilterDTO> {

}