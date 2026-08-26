package d3.authentication.infrastructure;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.authentication.domain.UsuarioSesionDTO;
import d3.authentication.domain.UsuarioSesionFilterDTO;
import d3.java.domain.IBasicMapper;

@D3SqlConnMapper(value = "UsuarioSesionMapper")
public interface UsuarioSesionMapper extends IBasicMapper<UsuarioSesionDTO, UsuarioSesionFilterDTO> {

	void closeAllSession(@Param("userId") String userId, @Param("token") String token);

	String obtenerPrincipal();

	String obtenerOrganizacion();

	String obtenerPrincipalMail();

}