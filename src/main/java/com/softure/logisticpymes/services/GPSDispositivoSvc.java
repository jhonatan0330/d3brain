package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.GPSDispositivoDTO;
import com.softure.logisticpymes.domain.filter.GPSDispositivoFilterDTO;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.GPSDispositivoMapper;

@Service("gPSDispositivoService")
public class GPSDispositivoSvc extends BasicSvc<GPSDispositivoDTO, GPSDispositivoFilterDTO> {
	
	@Autowired
	private GPSDispositivoMapper gPSDispositivoMapper;
	
	// BEGIN region servicesGPSDispositivo
	// END region servicesGPSDispositivo

	@Override
	public GPSDispositivoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. GPSDispositivo");
		GPSDispositivoFilterDTO dto = new GPSDispositivoFilterDTO();
		dto.setLlaveTabla(llave);
		return gPSDispositivoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = gPSDispositivoMapper;
	}
	
	@Override
	public GPSDispositivoDTO activar(GPSDispositivoDTO dto, String token) throws ServerException {
		// BEGIN GPSDispositivo_activar
		return super.activar(dto, token);
		// END GPSDispositivo_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public GPSDispositivoDTO actualizar( GPSDispositivoDTO dto, String token) throws ServerException {
		// BEGIN GPSDispositivo_actualizar
		return super.actualizar(dto, token);
		// END GPSDispositivo_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public GPSDispositivoDTO inactivar(GPSDispositivoDTO dto, String token) throws ServerException {
		// BEGIN GPSDispositivo_inactivar
		return super.inactivar(dto, token);
		// END GPSDispositivo_inactivar
	}
	
	@Override
	public GPSDispositivoDTO consultaUnica(GPSDispositivoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(GPSDispositivoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<GPSDispositivoDTO> listarConsulta(GPSDispositivoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public GPSDispositivoDTO guardar(GPSDispositivoDTO dto, String token) throws ServerException {
		// BEGIN GPSDispositivo_guardar
		return super.guardar(dto, token);
		// END GPSDispositivo_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}