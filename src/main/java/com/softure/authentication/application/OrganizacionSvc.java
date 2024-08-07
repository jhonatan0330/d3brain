package com.softure.authentication.application;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.OrganizacionFilterDTO;
import com.softure.authentication.infrastructure.OrganizacionMapper;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public OrganizacionDTO actualizar( OrganizacionDTO dto, String token) throws ServerException {
		// BEGIN Organizacion_actualizar
		return super.actualizar(dto, token);
		// END Organizacion_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	
	public OrganizacionDTO obtenerPrincipalPublic()throws ServerException{
		try {
			OrganizacionDTO result = organizacionMapper.obtenerPrincipal();
			result.setPropiedades(configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.ORGANIZACION, result.getLlaveTabla(), Propiedades.LANDING_PAGE, null));
			return result; 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public OrganizacionDTO obtenerPrincipal()throws ServerException{
		try {
			return organizacionMapper.obtenerPrincipal();
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	
	public OrganizacionDTO obtenerPrincipalPropiedades(String user)throws ServerException{
		OrganizacionDTO result = obtenerPrincipal();
		if (result !=null) {
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