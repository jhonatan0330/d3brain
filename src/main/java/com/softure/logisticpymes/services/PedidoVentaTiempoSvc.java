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
import com.softure.logisticpymes.dto.PedidoVentaTiempoDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaTiempoFilterDTO;
import com.softure.logisticpymes.persistence.PedidoVentaTiempoMapper;

@Service("pedidoVentaTiempoService")
public class PedidoVentaTiempoSvc extends BasicSvc<PedidoVentaTiempoDTO, PedidoVentaTiempoFilterDTO> {
	
	@Autowired
	private PedidoVentaTiempoMapper pedidoVentaTiempoMapper;
	
	// BEGIN region servicesPedidoVentaTiempo
	// END region servicesPedidoVentaTiempo

	@Override
	public PedidoVentaTiempoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PedidoVentaTiempo");
		PedidoVentaTiempoFilterDTO dto = new PedidoVentaTiempoFilterDTO();
		dto.setLlaveTabla(llave);
		return pedidoVentaTiempoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = pedidoVentaTiempoMapper;
	}
	
	@Override
	public PedidoVentaTiempoDTO activar(PedidoVentaTiempoDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaTiempo_activar
		return super.activar(dto, token);
		// END PedidoVentaTiempo_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaTiempoDTO actualizar( PedidoVentaTiempoDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaTiempo_actualizar
		return super.actualizar(dto, token);
		// END PedidoVentaTiempo_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaTiempoDTO inactivar(PedidoVentaTiempoDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaTiempo_inactivar
		return super.inactivar(dto, token);
		// END PedidoVentaTiempo_inactivar
	}
	
	@Override
	public PedidoVentaTiempoDTO consultaUnica(PedidoVentaTiempoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PedidoVentaTiempoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PedidoVentaTiempoDTO> listarConsulta(PedidoVentaTiempoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaTiempoDTO guardar(PedidoVentaTiempoDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaTiempo_guardar
		return super.guardar(dto, token);
		// END PedidoVentaTiempo_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}