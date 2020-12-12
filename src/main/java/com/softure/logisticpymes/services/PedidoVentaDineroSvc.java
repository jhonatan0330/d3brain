package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;

import com.softure.java.cons.ConstantesGenerales;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaDineroFilterDTO;
import com.softure.logisticpymes.persistence.PedidoVentaDineroMapper;

@Service("pedidoVentaDineroService")
public class PedidoVentaDineroSvc extends BasicSvc<PedidoVentaDineroDTO, PedidoVentaDineroFilterDTO> {
	
	@Autowired
	private PedidoVentaDineroMapper pedidoVentaDineroMapper;
	
	// BEGIN region servicesPedidoVentaDinero
	// END region servicesPedidoVentaDinero

	@Override
	public PedidoVentaDineroDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PedidoVentaDinero");
		PedidoVentaDineroFilterDTO dto = new PedidoVentaDineroFilterDTO();
		dto.setLlaveTabla(llave);
		return pedidoVentaDineroMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = pedidoVentaDineroMapper;
	}
	
	@Override
	public PedidoVentaDineroDTO activar(PedidoVentaDineroDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaDinero_activar
		return super.activar(dto, token);
		// END PedidoVentaDinero_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaDineroDTO actualizar( PedidoVentaDineroDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaDinero_actualizar
		throw new ServerException("Metodo inactivo usar guardar");
		// END PedidoVentaDinero_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaDineroDTO inactivar(PedidoVentaDineroDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaDinero_inactivar
		return super.inactivar(dto, token);
		// END PedidoVentaDinero_inactivar
	}
	
	@Override
	public PedidoVentaDineroDTO consultaUnica(PedidoVentaDineroFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PedidoVentaDineroFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PedidoVentaDineroDTO> listarConsulta(PedidoVentaDineroFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaDineroDTO guardar(PedidoVentaDineroDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaDinero_guardar
		dto.setFecha(new Date());
		return super.guardar(dto, token);
		// END PedidoVentaDinero_guardar
	}

// BEGIN region aditionalMethods
	public PedidoVentaDineroDTO consultaPorDocumento(String documento) throws ServerException {
		PedidoVentaDineroFilterDTO filtro = new PedidoVentaDineroFilterDTO();
		filtro.setDocumento(documento);
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		return super.consultaUnica(filtro);
	}
	
	public List<PedidoVentaDineroDTO> listar2DocumentoVisible(List<PedidoVentaDTO> documentos)
			throws ServerException {//La plantilla es para optimizar la consultas de la particion
		if(documentos==null || documentos.isEmpty()) return null;
		return pedidoVentaDineroMapper.listar2DocumentoVisible(documentos);
	}
// END region aditionalMethods

}