package com.softure.money.application;

import java.util.List;

// BEGIN region interImport
import java.math.BigDecimal;

import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.cons.ConstantesGenerales;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.money.domain.CuentaDTO;
import com.softure.money.domain.CuentaFilterDTO;
import com.softure.money.infrastructure.CuentaMapper;

@Service("cuentaService")
public class CuentaSvc extends BasicSvc<CuentaDTO, CuentaFilterDTO> {
	
	@Autowired
	private CuentaMapper cuentaMapper;
	
	// BEGIN region servicesCuenta
	// END region servicesCuenta

	@Override
	public CuentaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Cuenta");
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
		// BEGIN Cuenta_activar
		return super.activar(dto, token);
		// END Cuenta_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaDTO actualizar( CuentaDTO dto, String token) throws ServerException {
		// BEGIN Cuenta_actualizar
		CuentaDTO cuenta = consultaXId(dto.getLlaveTabla());
		if(cuenta.getFechaConciliacion()!=null){
			if(dto.getFechaConciliacion()==null) dto.setFechaConciliacion(cuenta.getFechaConciliacion());
			if(dto.getFechaConciliacion().before(cuenta.getFechaConciliacion()))throw new ServerException("La nueva fecha de conciliacion no puede ser menor a " + cuenta.getFechaConciliacion().toString());
		}
		return super.actualizar(dto, token);
		// END Cuenta_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaDTO inactivar(CuentaDTO dto, String token) throws ServerException {
		// BEGIN Cuenta_inactivar
		return super.inactivar(dto, token);
		// END Cuenta_inactivar
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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaDTO guardar(CuentaDTO dto, String token) throws ServerException {
		// BEGIN Cuenta_guardar
		return super.guardar(dto, token);
		// END Cuenta_guardar
	}

// BEGIN region aditionalMethods
	public CuentaDTO crearCuenta(PedidoVentaDTO dto, String token) throws ServerException {
		CuentaDTO cuentaNueva = new CuentaDTO();
		cuentaNueva.setCodigo(dto.getNombre());
		cuentaNueva.setNombre(dto.getDescripcion());
		cuentaNueva.setDocumento(dto.getLlaveTabla());
		return guardar(cuentaNueva, token);
	}
	
	public void inactivarDocumento(PedidoVentaDTO dto, String token) throws ServerException {
		CuentaFilterDTO cuentaFilter = new CuentaFilterDTO();
		cuentaFilter.setDocumento(dto.getLlaveTabla());
		cuentaFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		CuentaDTO cuenta = consultaUnica(cuentaFilter);
		if(cuenta!=null){
			inactivar(cuenta, token);
		}
	}
	
	public BigDecimal sobregiro(String cuentaId) {
		return new BigDecimal( cuentaMapper.sobregiro(cuentaId) );
	}
// END region aditionalMethods

}