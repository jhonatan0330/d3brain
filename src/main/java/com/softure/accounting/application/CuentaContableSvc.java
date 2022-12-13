package com.softure.accounting.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.accounting.domain.CuentaContableDTO;
import com.softure.accounting.domain.CuentaContableFilterDTO;
import com.softure.accounting.infrastructure.CuentaContableMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("cuentaContableService")
public class CuentaContableSvc extends BasicSvc<CuentaContableDTO, CuentaContableFilterDTO> {
	
	@Autowired
	private CuentaContableMapper cuentaContableMapper;
	
	// BEGIN region servicesCuentaContable
	// END region servicesCuentaContable

	@Override
	public CuentaContableDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CuentaContable");
		CuentaContableFilterDTO dto = new CuentaContableFilterDTO();
		dto.setLlaveTabla(llave);
		return cuentaContableMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cuentaContableMapper;
	}
	
	@Override
	public CuentaContableDTO activar(CuentaContableDTO dto, String token) throws ServerException {
		// BEGIN CuentaContable_activar
		return super.activar(dto, token);
		// END CuentaContable_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaContableDTO actualizar( CuentaContableDTO dto, String token) throws ServerException {
		// BEGIN CuentaContable_actualizar
		return super.actualizar(dto, token);
		// END CuentaContable_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaContableDTO inactivar(CuentaContableDTO dto, String token) throws ServerException {
		// BEGIN CuentaContable_inactivar
		return super.inactivar(dto, token);
		// END CuentaContable_inactivar
	}
	
	@Override
	public CuentaContableDTO consultaUnica(CuentaContableFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CuentaContableFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CuentaContableDTO> listarConsulta(CuentaContableFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaContableDTO guardar(CuentaContableDTO dto, String token) throws ServerException {
		// BEGIN CuentaContable_guardar
		return super.guardar(dto, token);
		// END CuentaContable_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}