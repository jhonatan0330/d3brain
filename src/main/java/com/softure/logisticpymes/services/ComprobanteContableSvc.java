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
import com.softure.logisticpymes.dto.ComprobanteContableDTO;
import com.softure.logisticpymes.dto.filter.ComprobanteContableFilterDTO;
import com.softure.logisticpymes.persistence.ComprobanteContableMapper;

@Service("comprobanteContableService")
public class ComprobanteContableSvc extends BasicSvc<ComprobanteContableDTO, ComprobanteContableFilterDTO> {
	
	@Autowired
	private ComprobanteContableMapper comprobanteContableMapper;
	
	// BEGIN region servicesComprobanteContable
	// END region servicesComprobanteContable

	@Override
	public ComprobanteContableDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ComprobanteContable");
		ComprobanteContableFilterDTO dto = new ComprobanteContableFilterDTO();
		dto.setLlaveTabla(llave);
		return comprobanteContableMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = comprobanteContableMapper;
	}
	
	@Override
	public ComprobanteContableDTO activar(ComprobanteContableDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteContable_activar
		return super.activar(dto, token);
		// END ComprobanteContable_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteContableDTO actualizar( ComprobanteContableDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteContable_actualizar
		return super.actualizar(dto, token);
		// END ComprobanteContable_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteContableDTO inactivar(ComprobanteContableDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteContable_inactivar
		return super.inactivar(dto, token);
		// END ComprobanteContable_inactivar
	}
	
	@Override
	public ComprobanteContableDTO consultaUnica(ComprobanteContableFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ComprobanteContableFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ComprobanteContableDTO> listarConsulta(ComprobanteContableFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteContableDTO guardar(ComprobanteContableDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteContable_guardar
		return super.guardar(dto, token);
		// END ComprobanteContable_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}