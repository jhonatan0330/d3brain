package d3.authentication.infrastructure;

import d3.D3SqlConnMapper;
import d3.authentication.domain.UsuarioAutenticacionDTO;
import d3.authentication.domain.UsuarioAutenticacionFilterDTO;
import d3.shared.domain.IBasicMapper;

@D3SqlConnMapper(value = "UsuarioAutenticacionMapper")
public interface UsuarioAutenticacionMapper
		extends IBasicMapper<UsuarioAutenticacionDTO, UsuarioAutenticacionFilterDTO> {

	String consultarValidez();

	String versionActual();

	String fechaMinima();

	int cantidadAsignaciones(String usuario);

}