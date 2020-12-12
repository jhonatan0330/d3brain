package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.math.BigDecimal;
import java.util.Date;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.dto.ProductoDTO;
import com.softure.logisticpymes.dto.ProductoInventarioDTO;
import com.softure.logisticpymes.dto.filter.ProductoInventarioFilterDTO;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.TrazabilidadProductoInventarioDTO;
import com.softure.logisticpymes.dto.filter.TrazabilidadProductoInventarioFilterDTO;
import com.softure.logisticpymes.persistence.TrazabilidadProductoInventarioMapper;

@Service("trazabilidadProductoInventarioService")
public class TrazabilidadProductoInventarioSvc extends BasicSvc<TrazabilidadProductoInventarioDTO, TrazabilidadProductoInventarioFilterDTO> {
	
	@Autowired
	private TrazabilidadProductoInventarioMapper trazabilidadProductoInventarioMapper;
	
	// BEGIN region servicesTrazabilidadProductoInventario
	@Autowired ProductoInventarioSvc productoInventarioService;
	@Autowired ProductoSvc productoService;
	// END region servicesTrazabilidadProductoInventario

	@Override
	public TrazabilidadProductoInventarioDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. TrazabilidadProductoInventario");
		TrazabilidadProductoInventarioFilterDTO dto = new TrazabilidadProductoInventarioFilterDTO();
		dto.setLlaveTabla(llave);
		return trazabilidadProductoInventarioMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = trazabilidadProductoInventarioMapper;
	}
	
	@Override
	public TrazabilidadProductoInventarioDTO activar(TrazabilidadProductoInventarioDTO dto, String token) throws ServerException {
		// BEGIN TrazabilidadProductoInventario_activar
		return super.activar(dto, token);
		// END TrazabilidadProductoInventario_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TrazabilidadProductoInventarioDTO actualizar( TrazabilidadProductoInventarioDTO dto, String token) throws ServerException {
		// BEGIN TrazabilidadProductoInventario_actualizar
		return super.actualizar(dto, token);
		// END TrazabilidadProductoInventario_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TrazabilidadProductoInventarioDTO inactivar(TrazabilidadProductoInventarioDTO dto, String token) throws ServerException {
		// BEGIN TrazabilidadProductoInventario_inactivar
		return super.inactivar(dto, token);
		// END TrazabilidadProductoInventario_inactivar
	}
	
	@Override
	public TrazabilidadProductoInventarioDTO consultaUnica(TrazabilidadProductoInventarioFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(TrazabilidadProductoInventarioFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<TrazabilidadProductoInventarioDTO> listarConsulta(TrazabilidadProductoInventarioFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TrazabilidadProductoInventarioDTO guardar(TrazabilidadProductoInventarioDTO dto, String token) throws ServerException {
		// BEGIN TrazabilidadProductoInventario_guardar
		TrazabilidadProductoInventarioDTO trazabilidad = dto;
		if(trazabilidad.getCantidad()== null)
			throw new ServerException("La cantidad del movimiento de producto no puede ser null");
		if (trazabilidad.getFecha()==null) trazabilidad.setFecha(new Date());
		
		ProductoInventarioFilterDTO inventarioFilter = new ProductoInventarioFilterDTO();
		inventarioFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		inventarioFilter.setProducto(dto.getProducto());
		inventarioFilter.setBodega(dto.getBodega());
		ProductoInventarioDTO inventario = productoInventarioService.consultaUnica(inventarioFilter);
		
		//LOGICA DE INVENTARIOS
		if(inventario!=null){
			ProductoDTO producto = productoService.consultaXId(inventario.getProducto());
			if(producto.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("El producto no se encuentra activo: " + producto.getNombre());
			trazabilidad.setCantidadInicial(inventario.getCantidadActual());
			trazabilidad.setCantidadFinal(trazabilidad.getCantidadInicial().add(trazabilidad.getCantidad()));
			//Valido los tamanos de inventario catindad maxima y el cero
			if(trazabilidad.getCantidadFinal().compareTo(BigDecimal.ZERO)<0) throw new ServerException("No se puede deducir mas cantidad a la actual del producto.\nProducto:"+ inventario.getNombre() +"\nCant:" + inventario.getCantidadActual() + "\nSolicitado: " + trazabilidad.getCantidad() + "\nBodega: "+ inventario.getNombreBodega());
			if(trazabilidad.getCantidadFinal().compareTo(inventario.getCantidadMaxima())>0) throw new ServerException("Esta superando la cantidad maxima estimada del producto.\nProducto:" + inventario.getNombre() +"\nCant Maxima:" + inventario.getCantidadMaxima()+"\nCant Final:" + trazabilidad.getCantidadFinal());
			//Solo se acualiza hasta el final el produto para realizar los calculos en la trazabilidad
			inventario.setCantidadModificar(trazabilidad.getCantidad());
			trazabilidad.setResponsable(getUserFlex(token));
			trazabilidad = super.guardar(trazabilidad, token);
			inventario = productoInventarioService.actualizar(inventario, token);
		}
		return trazabilidad;
		// END TrazabilidadProductoInventario_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}