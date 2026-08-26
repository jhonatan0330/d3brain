package d3.authentication.infrastructure;

import java.util.List;

import d3.D3SqlConnMapper;
import d3.authentication.domain.OrganizacionDTO;
import d3.authentication.domain.OrganizacionFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "OrganizacionMapper")
public interface OrganizacionMapper extends IBasicMapper<OrganizacionDTO, OrganizacionFilterDTO> {

	List<OrganizacionDTO> obtenerUsuario(String usuario);

	OrganizacionDTO obtenerPrincipal();
}