package com.accounting.voucher.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.voucher.domain.ComprobanteConfiguracionDetalleDTO;
import com.accounting.voucher.domain.ComprobanteConfiguracionDetalleFilterDTO;
import com.accounting.voucher.infrastructure.ComprobanteConfiguracionDetalleMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("comprobanteConfiguracionDetalleService")
public class ComprobanteConfiguracionDetalleSvc extends BasicSvc<ComprobanteConfiguracionDetalleDTO, ComprobanteConfiguracionDetalleFilterDTO> {
	
	@Autowired
	private ComprobanteConfiguracionDetalleMapper comprobanteConfiguracionDetalleMapper;
	
	// BEGIN region servicesComprobanteConfiguracionDetalle
	// END region servicesComprobanteConfiguracionDetalle

	@Override
	public ComprobanteConfiguracionDetalleDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ComprobanteConfiguracionDetalle");
		ComprobanteConfiguracionDetalleFilterDTO dto = new ComprobanteConfiguracionDetalleFilterDTO();
		dto.setLlaveTabla(llave);
		return comprobanteConfiguracionDetalleMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = comprobanteConfiguracionDetalleMapper;
	}
	
	@Override
	public ComprobanteConfiguracionDetalleDTO activar(ComprobanteConfiguracionDetalleDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteConfiguracionDetalle_activar
		return super.activar(dto, token);
		// END ComprobanteConfiguracionDetalle_activar
	}
	
	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteConfiguracionDetalleDTO actualizar( ComprobanteConfiguracionDetalleDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteConfiguracionDetalle_actualizar
		return super.actualizar(dto, token);
		// END ComprobanteConfiguracionDetalle_actualizar
	}
	
	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteConfiguracionDetalleDTO inactivar(ComprobanteConfiguracionDetalleDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteConfiguracionDetalle_inactivar
		return super.inactivar(dto, token);
		// END ComprobanteConfiguracionDetalle_inactivar
	}
	
	@Override
	public ComprobanteConfiguracionDetalleDTO consultaUnica(ComprobanteConfiguracionDetalleFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ComprobanteConfiguracionDetalleFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ComprobanteConfiguracionDetalleDTO> listarConsulta(ComprobanteConfiguracionDetalleFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteConfiguracionDetalleDTO guardar(ComprobanteConfiguracionDetalleDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteConfiguracionDetalle_guardar
		return super.guardar(dto, token);
		// END ComprobanteConfiguracionDetalle_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}