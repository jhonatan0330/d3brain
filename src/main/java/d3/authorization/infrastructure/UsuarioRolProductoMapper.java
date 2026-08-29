package d3.authorization.infrastructure;

import d3.D3SqlConnMapper;
import d3.authorization.domain.UsuarioRolProductoDTO;
import d3.authorization.domain.UsuarioRolProductoFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "UsuarioRolProductoMapper")
public interface UsuarioRolProductoMapper extends IBasicMapper<UsuarioRolProductoDTO, UsuarioRolProductoFilterDTO> {

}