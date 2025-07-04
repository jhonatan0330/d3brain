package com.softure.authentication.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.OrganizacionFilterDTO;
import com.softure.authentication.infrastructure.OrganizacionMapper;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

import jakarta.annotation.PostConstruct;

@Service("organizacionService")
public class OrganizacionSvc extends BasicSvc<OrganizacionDTO, OrganizacionFilterDTO> {

	@Autowired
	@Lazy
	private OrganizacionMapper organizacionMapper;
	@Autowired
	@Lazy
	private PropiedadSvc configuracionSvc;

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
	public List<OrganizacionDTO> listarConsulta(OrganizacionFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	public OrganizacionDTO obtenerPrincipalPublic(String ipRequest) throws ServerException {
		try {
			OrganizacionDTO result = organizacionMapper.obtenerPrincipal();
			// Por el momento no se usa el usuario publico, pero se deja comentado por si se requiere en el futuro
			
			//String userPublic = configuracionSvc.obtenerUnica(PropiedadValorDefinidoDTO.ORGANIZACION,
			//		result.getLlaveTabla(), Propiedades.PUBLIC_USER, null);
			//if (userPublic != null) {
			//	String token =  usuarioAutenticacionService.getTokenPublic(userPublic, ipRequest);
			//	result.setPublicToken(token);
			//}
			result.setPropiedades(configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.ORGANIZACION,
					result.getLlaveTabla(), null, null, false));
			return result;
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public OrganizacionDTO obtenerPrincipal() throws ServerException {
		try {
			return organizacionMapper.obtenerPrincipal();
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public OrganizacionDTO guardar(OrganizacionDTO dto, String token) throws ServerException {
		// BEGIN Organizacion_guardar
		return super.guardar(dto, token);
		// END Organizacion_guardar
	}

	public List<OrganizacionDTO> obtenerUsuario(String usuario) throws ServerException {
		try {
			List<OrganizacionDTO> organizaciones = organizacionMapper.obtenerUsuario(usuario);
			if (organizaciones != null && !organizaciones.isEmpty()) {
				for (OrganizacionDTO organizacionDTO : organizaciones) {
					organizacionDTO.setPropiedades(configuracionSvc.obtenerPropiedades(
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
			result.setPropiedades(configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.ORGANIZACION,
					result.getLlaveTabla(), null, user));
		}
		return result;
	}


}