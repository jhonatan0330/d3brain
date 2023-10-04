package com.accounting.plan.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.accounting.plan.domain.CatalogoDTO;
import com.accounting.plan.domain.CatalogoFilterDTO;
import com.accounting.plan.infrastructure.CatalogoMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("catalogoAccountingService")
public class CatalogoSvc extends BasicSvc<CatalogoDTO, CatalogoFilterDTO> {
	
	@Autowired
	private CatalogoMapper catalogoMapper;
	
	// BEGIN region servicescatalogo
	// END region servicescatalogo

	@Override
	public CatalogoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. catalogo");
		CatalogoFilterDTO dto = new CatalogoFilterDTO();
		dto.setLlaveTabla(llave);
		return catalogoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = catalogoMapper;
	}
	
	@Override
	public CatalogoDTO activar(CatalogoDTO dto, String token) throws ServerException {
		// BEGIN catalogo_activar
		return super.activar(dto, token);
		// END catalogo_activar
	}
	
	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CatalogoDTO actualizar( CatalogoDTO dto, String token) throws ServerException {
		// BEGIN catalogo_actualizar
		return super.actualizar(dto, token);
		// END catalogo_actualizar
	}
	
	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CatalogoDTO inactivar(CatalogoDTO dto, String token) throws ServerException {
		// BEGIN catalogo_inactivar
		return super.inactivar(dto, token);
		// END catalogo_inactivar
	}
	
	@Override
	public CatalogoDTO consultaUnica(CatalogoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CatalogoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CatalogoDTO> listarConsulta(CatalogoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "accountingTransactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CatalogoDTO guardar(CatalogoDTO dto, String token) throws ServerException {
		// BEGIN catalogo_guardar
		return super.guardar(dto, token);
		// END catalogo_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}