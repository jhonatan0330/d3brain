package com.softure.logisticpymes.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.domain.CambioDTO;
import com.softure.logisticpymes.domain.CambioFilterDTO;
import com.softure.logisticpymes.infrastructure.CambioMapper;

@Service("cambioService")
public class CambioSvc extends BasicSvc<CambioDTO, CambioFilterDTO> {
	
	@Autowired
	private CambioMapper cambioMapper;
	
	// BEGIN region servicesCambio
	// END region servicesCambio

	@Override
	public CambioDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Cambio");
		CambioFilterDTO dto = new CambioFilterDTO();
		dto.setLlaveTabla(llave);
		return cambioMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cambioMapper;
	}
	
	@Override
	public CambioDTO activar(CambioDTO dto, String token) throws ServerException {
		// BEGIN Cambio_activar
		return super.activar(dto, token);
		// END Cambio_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CambioDTO actualizar( CambioDTO dto, String token) throws ServerException {
		// BEGIN Cambio_actualizar
		dto.setSesionActiva(token);
		return super.actualizar(dto, token);
		// END Cambio_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CambioDTO inactivar(CambioDTO dto, String token) throws ServerException {
		// BEGIN Cambio_inactivar
		return super.inactivar(dto, token);
		// END Cambio_inactivar
	}
	
	@Override
	public CambioDTO consultaUnica(CambioFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CambioFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CambioDTO> listarConsulta(CambioFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CambioDTO guardar(CambioDTO dto, String token) throws ServerException {
		// BEGIN Cambio_guardar
		CambioFilterDTO filtro = new CambioFilterDTO();
		int cantidad = contarResultados(filtro);
		filtro = new CambioFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setSesionActiva(token);
		List<CambioDTO> activas = listarConsulta(filtro);
		if(activas!=null && activas.size()!=0) {
			for (CambioDTO cambioDTO : activas) {
				cambioDTO.setSesionActiva(null);
				super.update(cambioDTO);
			}
		}
		cantidad = cantidad+1;
		dto.setNombre("SC_"+ cantidad);
		dto.setFecha(new Date());
		dto.setSesionActiva(token);
		return super.guardar(dto, token);
		// END Cambio_guardar
	}

// BEGIN region aditionalMethods
	
	public CambioDTO obtenerCambioGrabando(String token) throws ServerException{
		CambioFilterDTO filtro = new CambioFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setSesionActiva(token);
		List<CambioDTO> cambios = listarConsulta(filtro);
		if(cambios==null || cambios.isEmpty()) throw new ServerException("CHANGE: No se encuentra un cambio activo y grabando para registrar el cambio");
		if(cambios.size()>1) throw new ServerException("Existen varios cambios grabando, solo puede estar uno al mismo tiempo. Cant. " + cambios.size());
		return cambios.get(0);
	}
// END region aditionalMethods

}