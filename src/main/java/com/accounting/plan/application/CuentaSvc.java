package com.accounting.plan.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.domain.CuentaDTO;
import com.accounting.plan.domain.CuentaFilterDTO;
import com.accounting.plan.infrastructure.CuentaMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("cuentaAccountingService")
public class CuentaSvc extends BasicSvc<CuentaDTO, CuentaFilterDTO> {
	
	@Autowired
	private CuentaMapper cuentaMapper;
	
	// BEGIN region servicescuenta
	// END region servicescuenta

	@Override
	public CuentaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. cuenta");
		CuentaFilterDTO dto = new CuentaFilterDTO();
		dto.setLlaveTabla(llave);
		return cuentaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cuentaMapper;
	}
	
	@Override
	public CuentaDTO activar(CuentaDTO dto, String token) throws ServerException {
		// BEGIN cuenta_activar
		return super.activar(dto, token);
		// END cuenta_activar
	}
	
	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaDTO actualizar( CuentaDTO dto, String token) throws ServerException {
		// BEGIN cuenta_actualizar
		return super.actualizar(dto, token);
		// END cuenta_actualizar
	}
	
	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaDTO inactivar(CuentaDTO dto, String token) throws ServerException {
		// BEGIN cuenta_inactivar
		return super.inactivar(dto, token);
		// END cuenta_inactivar
	}
	
	@Override
	public CuentaDTO consultaUnica(CuentaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CuentaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CuentaDTO> listarConsulta(CuentaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaDTO guardar(CuentaDTO dto, String token) throws ServerException {
		// BEGIN cuenta_guardar
		return super.guardar(dto, token);
		// END cuenta_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}