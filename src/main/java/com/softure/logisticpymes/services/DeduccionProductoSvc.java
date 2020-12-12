package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
import java.math.BigDecimal;

import com.softure.logisticpymes.dto.TrazabilidadProductoInventarioDTO;
import com.softure.logisticpymes.dto.filter.TrazabilidadProductoInventarioFilterDTO;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DeduccionProductoDTO;
import com.softure.logisticpymes.dto.filter.DeduccionProductoFilterDTO;
import com.softure.logisticpymes.persistence.DeduccionProductoMapper;

@Service("deduccionProductoService")
public class DeduccionProductoSvc extends BasicSvc<DeduccionProductoDTO, DeduccionProductoFilterDTO> {
	
	@Autowired
	private DeduccionProductoMapper deduccionProductoMapper;
	
	// BEGIN region servicesDeduccionProducto
	@Autowired
	private  TrazabilidadProductoInventarioSvc trazabilidadProductoInventarioService;
	// END region servicesDeduccionProducto

	@Override
	public DeduccionProductoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. DeduccionProducto");
		DeduccionProductoFilterDTO dto = new DeduccionProductoFilterDTO();
		dto.setLlaveTabla(llave);
		return deduccionProductoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = deduccionProductoMapper;
	}
	
	@Override
	public DeduccionProductoDTO activar(DeduccionProductoDTO dto, String token) throws ServerException {
		// BEGIN DeduccionProducto_activar
		return super.activar(dto, token);
		// END DeduccionProducto_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DeduccionProductoDTO actualizar( DeduccionProductoDTO dto, String token) throws ServerException {
		// BEGIN DeduccionProducto_actualizar
		return super.actualizar(dto, token);
		// END DeduccionProducto_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DeduccionProductoDTO inactivar(DeduccionProductoDTO dto, String token) throws ServerException {
		// BEGIN DeduccionProducto_inactivar
		dto = super.inactivar(dto, token);
		TrazabilidadProductoInventarioFilterDTO trazabilidadFilter = new TrazabilidadProductoInventarioFilterDTO();
		trazabilidadFilter.setDeduccionProducto(dto.getLlaveTabla());
		List<TrazabilidadProductoInventarioDTO>trazas = trazabilidadProductoInventarioService.listarConsulta(trazabilidadFilter);
		if(trazas!=null && trazas.size()!=0){
			TrazabilidadProductoInventarioDTO trazabilidad = new TrazabilidadProductoInventarioDTO();
			trazabilidad.setProducto(dto.getProducto());
			trazabilidad.setCantidad(BigDecimal.ZERO);
			trazabilidad.setBodega(dto.getBodega());
			for (TrazabilidadProductoInventarioDTO trazabilidadProductoInventarioDTO : trazas) {
				trazabilidad.setCantidad(trazabilidad.getCantidad().add(trazabilidadProductoInventarioDTO.getCantidad().negate()));
			}
			trazabilidad.setDeduccionProducto(dto.getLlaveTabla());
			trazabilidadProductoInventarioService.guardar(trazabilidad, token);
		}
		return dto;
		// END DeduccionProducto_inactivar
	}
	
	@Override
	public DeduccionProductoDTO consultaUnica(DeduccionProductoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(DeduccionProductoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<DeduccionProductoDTO> listarConsulta(DeduccionProductoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DeduccionProductoDTO guardar(DeduccionProductoDTO dto, String token) throws ServerException {
		// BEGIN DeduccionProducto_guardar
		if(dto.getFecha()==null) dto.setFecha(new Date());
		if(dto.getCantidad() == null || dto.getCantidad().compareTo(BigDecimal.ZERO)==0) throw new ServerException("No se puede realizar una deduccion sin cantidad");
		dto = super.guardar(dto, token);
		TrazabilidadProductoInventarioDTO trazabilidad = new TrazabilidadProductoInventarioDTO();
		trazabilidad.setProducto(dto.getProducto());
		trazabilidad.setCantidad(dto.getCantidad());
		trazabilidad.setBodega(dto.getBodega());
		trazabilidad.setDeduccionProducto(dto.getLlaveTabla());
		trazabilidad = trazabilidadProductoInventarioService.guardar(trazabilidad, token);
		return dto;
		// END DeduccionProducto_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}