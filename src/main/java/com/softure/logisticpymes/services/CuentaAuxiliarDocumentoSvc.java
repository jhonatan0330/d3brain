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
import com.softure.logisticpymes.dto.CuentaAuxiliarDocumentoDTO;
import com.softure.logisticpymes.dto.filter.CuentaAuxiliarDocumentoFilterDTO;
import com.softure.logisticpymes.persistence.CuentaAuxiliarDocumentoMapper;

@Service("cuentaAuxiliarDocumentoService")
public class CuentaAuxiliarDocumentoSvc extends BasicSvc<CuentaAuxiliarDocumentoDTO, CuentaAuxiliarDocumentoFilterDTO> {
	
	@Autowired
	private CuentaAuxiliarDocumentoMapper cuentaAuxiliarDocumentoMapper;
	
	// BEGIN region servicesCuentaAuxiliarDocumento
	// END region servicesCuentaAuxiliarDocumento

	@Override
	public CuentaAuxiliarDocumentoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CuentaAuxiliarDocumento");
		CuentaAuxiliarDocumentoFilterDTO dto = new CuentaAuxiliarDocumentoFilterDTO();
		dto.setLlaveTabla(llave);
		return cuentaAuxiliarDocumentoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cuentaAuxiliarDocumentoMapper;
	}
	
	@Override
	public CuentaAuxiliarDocumentoDTO activar(CuentaAuxiliarDocumentoDTO dto, String token) throws ServerException {
		// BEGIN CuentaAuxiliarDocumento_activar
		return super.activar(dto, token);
		// END CuentaAuxiliarDocumento_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaAuxiliarDocumentoDTO actualizar( CuentaAuxiliarDocumentoDTO dto, String token) throws ServerException {
		// BEGIN CuentaAuxiliarDocumento_actualizar
		return super.actualizar(dto, token);
		// END CuentaAuxiliarDocumento_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaAuxiliarDocumentoDTO inactivar(CuentaAuxiliarDocumentoDTO dto, String token) throws ServerException {
		// BEGIN CuentaAuxiliarDocumento_inactivar
		return super.inactivar(dto, token);
		// END CuentaAuxiliarDocumento_inactivar
	}
	
	@Override
	public CuentaAuxiliarDocumentoDTO consultaUnica(CuentaAuxiliarDocumentoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CuentaAuxiliarDocumentoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CuentaAuxiliarDocumentoDTO> listarConsulta(CuentaAuxiliarDocumentoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CuentaAuxiliarDocumentoDTO guardar(CuentaAuxiliarDocumentoDTO dto, String token) throws ServerException {
		// BEGIN CuentaAuxiliarDocumento_guardar
		return super.guardar(dto, token);
		// END CuentaAuxiliarDocumento_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}