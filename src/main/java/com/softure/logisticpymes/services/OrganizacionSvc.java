package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.OrganizacionDTO;
import com.softure.logisticpymes.dto.filter.OrganizacionFilterDTO;
import com.softure.logisticpymes.persistence.OrganizacionMapper;

@Service("organizacionService")
public class OrganizacionSvc extends BasicSvc<OrganizacionDTO, OrganizacionFilterDTO> {
	
	@Autowired
	private OrganizacionMapper organizacionMapper;
	
	// BEGIN region servicesOrganizacion
	@Autowired private PropiedadSvc configuracionSvc;
	// END region servicesOrganizacion

	@Override
	public OrganizacionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Organizacion");
		OrganizacionFilterDTO dto = new OrganizacionFilterDTO();
		dto.setLlaveTabla(llave);
		return organizacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = organizacionMapper;
	}
	
	@Override
	public OrganizacionDTO activar(OrganizacionDTO dto, String token) throws ServerException {
		// BEGIN Organizacion_activar
		return super.activar(dto, token);
		// END Organizacion_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public OrganizacionDTO actualizar( OrganizacionDTO dto, String token) throws ServerException {
		// BEGIN Organizacion_actualizar
		return super.actualizar(dto, token);
		// END Organizacion_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public OrganizacionDTO inactivar(OrganizacionDTO dto, String token) throws ServerException {
		// BEGIN Organizacion_inactivar
		return super.inactivar(dto, token);
		// END Organizacion_inactivar
	}
	
	@Override
	public OrganizacionDTO consultaUnica(OrganizacionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(OrganizacionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<OrganizacionDTO> listarConsulta(OrganizacionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public OrganizacionDTO obtenerPrincipal(OrganizacionFilterDTO dto)throws ServerException{
		// BEGIN region obtenerPrincipal
		// @generated
		// END region obtenerPrincipal
		paginar(dto);
		try {
			return organizacionMapper.obtenerPrincipal(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public OrganizacionDTO guardar(OrganizacionDTO dto, String token) throws ServerException {
		// BEGIN Organizacion_guardar
		return super.guardar(dto, token);
		// END Organizacion_guardar
	}

// BEGIN region aditionalMethods
	public List<OrganizacionDTO> obtenerUsuario(String usuario)throws ServerException{
		try {
			List<OrganizacionDTO> organizaciones = organizacionMapper.obtenerUsuario(usuario);
			if (organizaciones !=null && !organizaciones.isEmpty()) {
				for (OrganizacionDTO organizacionDTO : organizaciones) {
					organizacionDTO.setPropiedades(configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.ORGANIZACION, organizacionDTO.getLlaveTabla(), null, null));
				}
			}
			return organizaciones;
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public OrganizacionDTO obtenerPrincipalPropiedades(OrganizacionFilterDTO dto)throws ServerException{
		OrganizacionDTO result = obtenerPrincipal(dto);
		String user = null;
		if (dto.getSecurityToken() !=null) user = getUserFlex(dto.getSecurityToken());
		if(result !=null) {
			result.setPropiedades(configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.ORGANIZACION, result.getLlaveTabla(), null, user));
		}
		return result;
	}
	
	/*
	public OrganizacionDTO copiar(String org, String token) throws ServerException{
		OrganizacionDTO bd = consultaXId(org);
		// ASumo que la bd ya esta creada
		ProcesoFilterDTO filtroP = new ProcesoFilterDTO();
		filtroP.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<ProcesoDTO> proceso = procesoService.listarConsulta(filtroP);
		DbContextHolder.setDataSourceType(DbType.SLAVE);
		try {
			for (ProcesoDTO procesoDTO : proceso) {
				procesoService.guardar(procesoDTO, token);
			}
			//necesito 
		} catch (Exception e) {
			// TODO: handle exception
		}
		DbContextHolder.resetDataSourceType();		
		return bd;
	}*/
// END region aditionalMethods

}