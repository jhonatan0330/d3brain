package com.softure.accounting.application.services.adapter;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.accounting.domain.dto.ComprobanteConfiguracionDTO;
import com.softure.accounting.domain.filter.ComprobanteConfiguracionFilterDTO;
import com.softure.accounting.infrastructure.mybatis.mapper.ComprobanteConfiguracionMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.services.BasicSvc;

@Service("comprobanteConfiguracionService")
public class ComprobanteConfiguracionSvc extends BasicSvc<ComprobanteConfiguracionDTO, ComprobanteConfiguracionFilterDTO> {
	
	@Autowired
	private ComprobanteConfiguracionMapper comprobanteConfiguracionMapper;
	
	// BEGIN region servicesComprobanteConfiguracion
	// END region servicesComprobanteConfiguracion

	@Override
	public ComprobanteConfiguracionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ComprobanteConfiguracion");
		ComprobanteConfiguracionFilterDTO dto = new ComprobanteConfiguracionFilterDTO();
		dto.setLlaveTabla(llave);
		return comprobanteConfiguracionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = comprobanteConfiguracionMapper;
	}
	
	@Override
	public ComprobanteConfiguracionDTO activar(ComprobanteConfiguracionDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteConfiguracion_activar
		return super.activar(dto, token);
		// END ComprobanteConfiguracion_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteConfiguracionDTO actualizar( ComprobanteConfiguracionDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteConfiguracion_actualizar
		return super.actualizar(dto, token);
		// END ComprobanteConfiguracion_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteConfiguracionDTO inactivar(ComprobanteConfiguracionDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteConfiguracion_inactivar
		return super.inactivar(dto, token);
		// END ComprobanteConfiguracion_inactivar
	}
	
	@Override
	public ComprobanteConfiguracionDTO consultaUnica(ComprobanteConfiguracionFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ComprobanteConfiguracionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ComprobanteConfiguracionDTO> listarConsulta(ComprobanteConfiguracionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteConfiguracionDTO guardar(ComprobanteConfiguracionDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteConfiguracion_guardar
		return super.guardar(dto, token);
		// END ComprobanteConfiguracion_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}