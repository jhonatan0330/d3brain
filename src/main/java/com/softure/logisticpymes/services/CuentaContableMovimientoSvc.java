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
import com.softure.logisticpymes.domain.dto.CuentaContableMovimientoDTO;
import com.softure.logisticpymes.domain.filter.CuentaContableMovimientoFilterDTO;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.CuentaContableMovimientoMapper;

@Service("cuentaContableMovimientoService")
public class CuentaContableMovimientoSvc extends BasicSvc<CuentaContableMovimientoDTO, CuentaContableMovimientoFilterDTO> {
	
	@Autowired
	private CuentaContableMovimientoMapper cuentaContableMovimientoMapper;
	
	// BEGIN region servicesCuentaContableMovimiento
	// END region servicesCuentaContableMovimiento

	@Override
	public CuentaContableMovimientoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CuentaContableMovimiento");
		CuentaContableMovimientoFilterDTO dto = new CuentaContableMovimientoFilterDTO();
		dto.setLlaveTabla(llave);
		return cuentaContableMovimientoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cuentaContableMovimientoMapper;
	}
	
	@Override
	public CuentaContableMovimientoDTO activar(CuentaContableMovimientoDTO dto, String token) throws ServerException {
		// BEGIN CuentaContableMovimiento_activar
		return super.activar(dto, token);
		// END CuentaContableMovimiento_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaContableMovimientoDTO actualizar( CuentaContableMovimientoDTO dto, String token) throws ServerException {
		// BEGIN CuentaContableMovimiento_actualizar
		return super.actualizar(dto, token);
		// END CuentaContableMovimiento_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaContableMovimientoDTO inactivar(CuentaContableMovimientoDTO dto, String token) throws ServerException {
		// BEGIN CuentaContableMovimiento_inactivar
		return super.inactivar(dto, token);
		// END CuentaContableMovimiento_inactivar
	}
	
	@Override
	public CuentaContableMovimientoDTO consultaUnica(CuentaContableMovimientoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CuentaContableMovimientoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CuentaContableMovimientoDTO> listarConsulta(CuentaContableMovimientoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaContableMovimientoDTO guardar(CuentaContableMovimientoDTO dto, String token) throws ServerException {
		// BEGIN CuentaContableMovimiento_guardar
		return super.guardar(dto, token);
		// END CuentaContableMovimiento_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}