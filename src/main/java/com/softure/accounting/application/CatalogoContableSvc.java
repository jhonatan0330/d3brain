package com.softure.accounting.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.accounting.domain.CatalogoContableDTO;
import com.softure.accounting.domain.CatalogoContableFilterDTO;
import com.softure.accounting.infrastructure.CatalogoContableMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("catalogoContableService")
public class CatalogoContableSvc extends BasicSvc<CatalogoContableDTO, CatalogoContableFilterDTO> {
	
	@Autowired
	private CatalogoContableMapper catalogoContableMapper;
	
	// BEGIN region servicesCatalogoContable
	// END region servicesCatalogoContable

	@Override
	public CatalogoContableDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CatalogoContable");
		CatalogoContableFilterDTO dto = new CatalogoContableFilterDTO();
		dto.setLlaveTabla(llave);
		return catalogoContableMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = catalogoContableMapper;
	}
	
	@Override
	public CatalogoContableDTO activar(CatalogoContableDTO dto, String token) throws ServerException {
		// BEGIN CatalogoContable_activar
		return super.activar(dto, token);
		// END CatalogoContable_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CatalogoContableDTO actualizar( CatalogoContableDTO dto, String token) throws ServerException {
		// BEGIN CatalogoContable_actualizar
		return super.actualizar(dto, token);
		// END CatalogoContable_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CatalogoContableDTO inactivar(CatalogoContableDTO dto, String token) throws ServerException {
		// BEGIN CatalogoContable_inactivar
		return super.inactivar(dto, token);
		// END CatalogoContable_inactivar
	}
	
	@Override
	public CatalogoContableDTO consultaUnica(CatalogoContableFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CatalogoContableFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CatalogoContableDTO> listarConsulta(CatalogoContableFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CatalogoContableDTO guardar(CatalogoContableDTO dto, String token) throws ServerException {
		// BEGIN CatalogoContable_guardar
		return super.guardar(dto, token);
		// END CatalogoContable_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}