package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.domain.dto.GPSDispositivoDTO;
import com.softure.logisticpymes.domain.dto.GPSLocalizacionDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.PropiedadDTO;
import com.softure.logisticpymes.domain.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.domain.dto.UsuarioRolDTO;
import com.softure.logisticpymes.domain.filter.GPSDispositivoFilterDTO;
import com.softure.logisticpymes.domain.filter.GPSLocalizacionFilterDTO;
import com.softure.logisticpymes.domain.filter.UsuarioRolFilterDTO;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.GPSLocalizacionMapper;
import com.softure.logisticpymes.services.adapter.Propiedades;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;

@Service("gPSLocalizacionService")
public class GPSLocalizacionSvc extends BasicSvc<GPSLocalizacionDTO, GPSLocalizacionFilterDTO> {
	
	@Autowired
	private GPSLocalizacionMapper gPSLocalizacionMapper;
	
	// BEGIN region servicesGPSLocalizacion
	@Autowired private GPSDispositivoSvc gpsDispositivoService;
	@Autowired private PedidoVentaSvc documentoService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private UsuarioRolSvc usuarioRolService;
	@Autowired private GPSDispositivoSvc dispositivoService;
	// END region servicesGPSLocalizacion

	@Override
	public GPSLocalizacionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. GPSLocalizacion");
		GPSLocalizacionFilterDTO dto = new GPSLocalizacionFilterDTO();
		dto.setLlaveTabla(llave);
		return gPSLocalizacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = gPSLocalizacionMapper;
	}
	
	@Override
	public GPSLocalizacionDTO activar(GPSLocalizacionDTO dto, String token) throws ServerException {
		// BEGIN GPSLocalizacion_activar
		return super.activar(dto, token);
		// END GPSLocalizacion_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public GPSLocalizacionDTO actualizar( GPSLocalizacionDTO dto, String token) throws ServerException {
		// BEGIN GPSLocalizacion_actualizar
		return super.actualizar(dto, token);
		// END GPSLocalizacion_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public GPSLocalizacionDTO inactivar(GPSLocalizacionDTO dto, String token) throws ServerException {
		// BEGIN GPSLocalizacion_inactivar
		return super.inactivar(dto, token);
		// END GPSLocalizacion_inactivar
	}
	
	@Override
	public GPSLocalizacionDTO consultaUnica(GPSLocalizacionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(GPSLocalizacionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<GPSLocalizacionDTO> listarConsulta(GPSLocalizacionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public GPSLocalizacionDTO guardar(GPSLocalizacionDTO dto, String token) throws ServerException {
		// BEGIN GPSLocalizacion_guardar
		if(dto==null) throw new ServerException("El objeto no puede ser vacio");
		dto.setFecha(new Date());
		GPSDispositivoDTO dispositivo = gpsDispositivoService.consultaXId(dto.getDispositivo());
		dispositivo.setUltimaConexion(dto.getFecha());
		gpsDispositivoService.save(dispositivo);
		dto = super.save(dto);
		return dto;
		// END GPSLocalizacion_guardar
	}

// BEGIN region aditionalMethods
	public List<GPSLocalizacionDTO> listarDocumento(GPSLocalizacionFilterDTO dto)throws ServerException {
		//Valido que ese documento si tenga fechas de GPS
		PedidoVentaDTO documento = documentoService.consultaXId(dto.getDocumento());
		if(documento==null) throw new ServerException("Este documento no existe");
		//Consulto que ese documento tenga usuario, tenga dispositivo
		PropiedadDTO propiedad = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.PLANTILLA, documento.getPlantilla(), Propiedades.GPS, getUserFlex(dto.getSecurityToken()));
		if(propiedad==null) throw new ServerException("Este documento no se encuentra configurado para mostrar GPS");
		//Consulto el usuario
		UsuarioRolFilterDTO filtroRol = new UsuarioRolFilterDTO();
		filtroRol.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtroRol.setDocumento(documento.getLlaveTabla());
		UsuarioRolDTO uRol = usuarioRolService.consultaUnica(filtroRol);
		if(uRol==null) throw new ServerException("Este documento no se encuentra relacionado a un usuario");
		//Consulto el dispositivo
		GPSDispositivoFilterDTO filtroDispositivo = new GPSDispositivoFilterDTO();
		filtroDispositivo.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtroDispositivo.setUsuario(uRol.getUsuario());
		GPSDispositivoDTO dispositivo = dispositivoService.consultaUnica(filtroDispositivo);
		if(dispositivo==null) throw new ServerException("El usuario no tiene relacionando un dispositivo");
		//con el dipsositivo filtro 
		GPSLocalizacionFilterDTO filtro = new GPSLocalizacionFilterDTO();
		filtro.setDispositivo(dispositivo.getLlaveTabla());
		//filtro.setFechaMin(fechaMin);
		return listarConsulta(filtro);
	}
// END region aditionalMethods

}