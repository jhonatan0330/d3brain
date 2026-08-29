package d3.authorization.infrastructure;

import d3.D3SqlConnMapper;
import d3.authorization.domain.UsuarioRolDTO;
import d3.authorization.domain.UsuarioRolFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "UsuarioRolMapper")
public interface UsuarioRolMapper extends IBasicMapper<UsuarioRolDTO, UsuarioRolFilterDTO> {

}