package d3.authentication.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.authentication.domain.UsuarioOrganizacionDTO;
import d3.authentication.domain.UsuarioOrganizacionFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "UsuarioOrganizacionMapper")
public interface UsuarioOrganizacionMapper extends IBasicMapper<UsuarioOrganizacionDTO, UsuarioOrganizacionFilterDTO> {

	List<UsuarioOrganizacionDTO> sincronizarUsuarios();
}