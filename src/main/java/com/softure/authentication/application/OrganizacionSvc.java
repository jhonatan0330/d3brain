package com.softure.authentication.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.OrganizacionFilterDTO;
import com.softure.authentication.infrastructure.OrganizacionMapper;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.application.PropertyGetWithCacheService;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

import jakarta.annotation.PostConstruct;

@Service("organizacionService")
public class OrganizacionSvc extends BasicSvc<OrganizacionDTO, OrganizacionFilterDTO> {

	@Autowired
	@Lazy
	private OrganizacionMapper organizacionMapper;
	@Autowired
	@Lazy
	private PropertyGetWithCacheService cacheService;

	private OrganizacionDTO mainOrganization;

	@Override
	public OrganizacionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Organizacion");
		OrganizacionFilterDTO dto = new OrganizacionFilterDTO();
		dto.setLlaveTabla(llave);
		return organizacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = organizacionMapper;
	}

	@Override
	public OrganizacionDTO actualizar(OrganizacionDTO dto, String token) throws ServerException {
		mainOrganization = null;
		return super.actualizar(dto, token);
	}

	@Override
	public List<OrganizacionDTO> listarConsulta(OrganizacionFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	public OrganizacionDTO obtenerPrincipalPublic(String ipRequest) throws ServerException {
		try {
			OrganizacionDTO result = obtenerPrincipal();
			// Por el momento no se usa el usuario publico, pero se deja comentado por si se
			// requiere en el futuro

			// String userPublic =
			// configuracionSvc.obtenerUnica(PropiedadValorDefinidoDTO.ORGANIZACION,
			// result.getLlaveTabla(), Propiedades.PUBLIC_USER, null);
			// if (userPublic != null) {
			// String token = usuarioAutenticacionService.getTokenPublic(userPublic,
			// ipRequest);
			// result.setPublicToken(token);
			// }
			result.setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.ORGANIZACION,
					result.getLlaveTabla(), null, null, false));
			return result;
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public OrganizacionDTO obtenerPrincipal() throws ServerException {
		if (this.mainOrganization != null)
			return mainOrganization;
		try {
			this.mainOrganization = organizacionMapper.obtenerPrincipal();
			return mainOrganization;
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<OrganizacionDTO> obtenerUsuario(String usuario) throws ServerException {
		try {
			List<OrganizacionDTO> organizaciones = organizacionMapper.obtenerUsuario(usuario);
			if (organizaciones != null && !organizaciones.isEmpty()) {
				for (OrganizacionDTO organizacionDTO : organizaciones) {
					organizacionDTO.setPropiedades(cacheService.obtenerPropiedades(
							PropiedadValorDefinidoDTO.ORGANIZACION, organizacionDTO.getLlaveTabla(), null, null));
				}
			}
			return organizaciones;
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public OrganizacionDTO obtenerPrincipalPropiedades(String user) throws ServerException {
		OrganizacionDTO result = obtenerPrincipal();
		if (result != null) {
			result.setPropiedades(cacheService.obtenerPropiedades(PropiedadValorDefinidoDTO.ORGANIZACION,
					result.getLlaveTabla(), null, user));
		}
		return result;
	}

	public boolean permisosCompletos(String user) throws ServerException {
		OrganizacionDTO _main = obtenerPrincipal();
		return (cacheService.obtenerPropiedad(PropiedadValorDefinidoDTO.ORGANIZACION, _main.getLlaveTabla(),
				Propiedades.APP_ADMIN, user) != null);
	}

}