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
import com.softure.logisticpymes.domain.dto.ComprobanteCuentaDetalleDTO;
import com.softure.logisticpymes.domain.filter.ComprobanteCuentaDetalleFilterDTO;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.ComprobanteCuentaDetalleMapper;

@Service("comprobanteCuentaDetalleService")
public class ComprobanteCuentaDetalleSvc extends BasicSvc<ComprobanteCuentaDetalleDTO, ComprobanteCuentaDetalleFilterDTO> {
	
	@Autowired
	private ComprobanteCuentaDetalleMapper comprobanteCuentaDetalleMapper;
	
	// BEGIN region servicesComprobanteCuentaDetalle
	// END region servicesComprobanteCuentaDetalle

	@Override
	public ComprobanteCuentaDetalleDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ComprobanteCuentaDetalle");
		ComprobanteCuentaDetalleFilterDTO dto = new ComprobanteCuentaDetalleFilterDTO();
		dto.setLlaveTabla(llave);
		return comprobanteCuentaDetalleMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = comprobanteCuentaDetalleMapper;
	}
	
	@Override
	public ComprobanteCuentaDetalleDTO activar(ComprobanteCuentaDetalleDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteCuentaDetalle_activar
		return super.activar(dto, token);
		// END ComprobanteCuentaDetalle_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteCuentaDetalleDTO actualizar( ComprobanteCuentaDetalleDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteCuentaDetalle_actualizar
		return super.actualizar(dto, token);
		// END ComprobanteCuentaDetalle_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteCuentaDetalleDTO inactivar(ComprobanteCuentaDetalleDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteCuentaDetalle_inactivar
		return super.inactivar(dto, token);
		// END ComprobanteCuentaDetalle_inactivar
	}
	
	@Override
	public ComprobanteCuentaDetalleDTO consultaUnica(ComprobanteCuentaDetalleFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ComprobanteCuentaDetalleFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ComprobanteCuentaDetalleDTO> listarConsulta(ComprobanteCuentaDetalleFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ComprobanteCuentaDetalleDTO guardar(ComprobanteCuentaDetalleDTO dto, String token) throws ServerException {
		// BEGIN ComprobanteCuentaDetalle_guardar
		return super.guardar(dto, token);
		// END ComprobanteCuentaDetalle_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}