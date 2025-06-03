package com.softure.inventory.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.inventory.domain.CategoriaProductoDTO;
import com.softure.inventory.domain.CategoriaProductoFilterDTO;
import com.softure.inventory.infrastructure.CategoriaProductoMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("categoriaProductoService")
public class CategoriaProductoSvc extends BasicSvc<CategoriaProductoDTO, CategoriaProductoFilterDTO> {
	
	@Autowired @Lazy 
	private CategoriaProductoMapper categoriaProductoMapper;

	@Override
	public CategoriaProductoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CategoriaProducto");
		CategoriaProductoFilterDTO dto = new CategoriaProductoFilterDTO();
		dto.setLlaveTabla(llave);
		return categoriaProductoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = categoriaProductoMapper;
	}
	
	@Override
	public CategoriaProductoDTO activar(CategoriaProductoDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CategoriaProductoDTO actualizar( CategoriaProductoDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CategoriaProductoDTO inactivar(CategoriaProductoDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
	}
	
	@Override
	public CategoriaProductoDTO consultaUnica(CategoriaProductoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CategoriaProductoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CategoriaProductoDTO> listarConsulta(CategoriaProductoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CategoriaProductoDTO guardar(CategoriaProductoDTO dto, String token) throws ServerException {
		if(dto.getPlantilla()==null)  throw new ServerException("Al crear una categoria de producto debe seleccionar una plantilla.");
		dto = super.guardar(dto, token);
		return dto;
	}


}