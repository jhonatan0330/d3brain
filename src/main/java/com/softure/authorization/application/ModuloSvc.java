package com.softure.authorization.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.authorization.domain.ModuloDTO;
import com.softure.authorization.domain.ModuloFilterDTO;
import com.softure.authorization.infrastructure.ModuloMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("moduloService")
public class ModuloSvc extends BasicSvc<ModuloDTO, ModuloFilterDTO> {
	
	@Autowired
	private ModuloMapper moduloMapper;
	
	// BEGIN region servicesModulo
	// END region servicesModulo

	@Override
	public ModuloDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Modulo");
		ModuloFilterDTO dto = new ModuloFilterDTO();
		dto.setLlaveTabla(llave);
		return moduloMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = moduloMapper;
	}
	
	@Override
	public ModuloDTO activar(ModuloDTO dto, String token) throws ServerException {
		// BEGIN Modulo_activar
		return super.activar(dto, token);
		// END Modulo_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ModuloDTO actualizar( ModuloDTO dto, String token) throws ServerException {
		// BEGIN Modulo_actualizar
		return super.actualizar(dto, token);
		// END Modulo_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ModuloDTO inactivar(ModuloDTO dto, String token) throws ServerException {
		// BEGIN Modulo_inactivar
		return super.inactivar(dto, token);
		// END Modulo_inactivar
	}
	
	@Override
	public ModuloDTO consultaUnica(ModuloFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ModuloFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ModuloDTO> listarConsulta(ModuloFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ModuloDTO guardar(ModuloDTO dto, String token) throws ServerException {
		// BEGIN Modulo_guardar
		return super.guardar(dto, token);
		// END Modulo_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}