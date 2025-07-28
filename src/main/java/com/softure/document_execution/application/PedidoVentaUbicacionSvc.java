package com.softure.document_execution.application;

import java.util.Date;

import org.apache.ibatis.binding.BindingException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.document_execution.domain.PedidoVentaUbicacionDTO;
import com.softure.document_execution.domain.PedidoVentaUbicacionFilterDTO;
import com.softure.document_execution.infrastructure.PedidoVentaUbicacionMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("pedidoVentaUbicacionService")
public class PedidoVentaUbicacionSvc extends BasicSvc<PedidoVentaUbicacionDTO, PedidoVentaUbicacionFilterDTO> {
	
	@Autowired @Lazy 
	private PedidoVentaUbicacionMapper pedidoVentaUbicacionMapper;
	

	@Override
	public PedidoVentaUbicacionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PedidoVentaDinero");
		PedidoVentaUbicacionFilterDTO dto = new PedidoVentaUbicacionFilterDTO();
		dto.setLlaveTabla(llave);
		return pedidoVentaUbicacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = pedidoVentaUbicacionMapper;
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaUbicacionDTO actualizar( PedidoVentaUbicacionDTO dto, String token) throws ServerException {
		throw new ServerException("Metodo inactivo usar guardar");
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaUbicacionDTO inactivar(PedidoVentaUbicacionDTO dto, String token) throws ServerException {
		throw new ServerException("Metodo inactivo usar inactivar ConHistorial");
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaUbicacionDTO guardar(PedidoVentaUbicacionDTO dto, String token) throws ServerException {
		throw new ServerException("Metodo inactivo usar guardar con historial");
	}

	public PedidoVentaUbicacionDTO consultaPorDocumento(String documento, Integer historico, String name) throws ServerException {
		try {
			return pedidoVentaUbicacionMapper.consultaPorDocumento(documento, historico, generarLlave());	
		} catch (BindingException ex) {
			throw new ServerException("El documento " + name + " tiene la siguiente novedad al consultar el valor : "  + ex.getMessage());
		} catch (MyBatisSystemException msex) {
			throw new ServerException("El documento " + name + " tiene la siguiente novedad al consultar el valor : "  + msex.getCause().getMessage());
		} catch (Exception e) {
			throw new ServerException("El documento " + name + " tiene la siguiente novedad al consultar el valor : "  + e.getMessage());
		}
	}
	
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaUbicacionDTO guardarConHistorial(PedidoVentaUbicacionDTO dto, Integer historico) throws ServerException {
		close(dto.getDocumento(), historico);
		dto.setFecha(new Date());
		if (historico ==null ) {
			return save(dto);
		} else {
			dto.setLlaveTabla(generarLlave());
			try {
				pedidoVentaUbicacionMapper.insertarHistorico(dto);
			}catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
			return dto;
		}
	}
	
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaUbicacionDTO inactivarConHistorial(PedidoVentaUbicacionDTO dto, Integer historico) throws ServerException {
		return pedidoVentaUbicacionMapper.inactivarHistorico(dto.getLlaveTabla(), (historico==null)?null:"Historico");
	}

	public void close(String pKey, Integer pHistoric) throws ServerException {
		PedidoVentaUbicacionDTO _last = consultaPorDocumento(pKey,pHistoric, "PedidoVentaUbicacion");
		if(_last==null) return;
		inactivarConHistorial(_last, pHistoric);
	}

}