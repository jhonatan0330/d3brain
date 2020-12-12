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
import com.softure.logisticpymes.dto.TarifarioDTO;
import com.softure.logisticpymes.dto.filter.TarifarioFilterDTO;
import com.softure.logisticpymes.persistence.TarifarioMapper;

@Service("tarifarioService")
public class TarifarioSvc extends BasicSvc<TarifarioDTO, TarifarioFilterDTO> {
	
	@Autowired
	private TarifarioMapper tarifarioMapper;
	
	// BEGIN region servicesTarifario
	// END region servicesTarifario

	@Override
	public TarifarioDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Tarifario");
		TarifarioFilterDTO dto = new TarifarioFilterDTO();
		dto.setLlaveTabla(llave);
		return tarifarioMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = tarifarioMapper;
	}
	
	@Override
	public TarifarioDTO activar(TarifarioDTO dto, String token) throws ServerException {
		// BEGIN Tarifario_activar
		return super.activar(dto, token);
		// END Tarifario_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TarifarioDTO actualizar( TarifarioDTO dto, String token) throws ServerException {
		// BEGIN Tarifario_actualizar
		return super.actualizar(dto, token);
		// END Tarifario_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TarifarioDTO inactivar(TarifarioDTO dto, String token) throws ServerException {
		// BEGIN Tarifario_inactivar
		return super.inactivar(dto, token);
		// END Tarifario_inactivar
	}
	
	@Override
	public TarifarioDTO consultaUnica(TarifarioFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(TarifarioFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<TarifarioDTO> listarConsulta(TarifarioFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TarifarioDTO guardar(TarifarioDTO dto, String token) throws ServerException {
		// BEGIN Tarifario_guardar
		return super.guardar(dto, token);
		// END Tarifario_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}