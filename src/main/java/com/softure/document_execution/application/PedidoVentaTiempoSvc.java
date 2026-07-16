package com.softure.document_execution.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.document_execution.domain.PedidoVentaTiempoDTO;
import com.softure.document_execution.domain.PedidoVentaTiempoFilterDTO;
import com.softure.document_execution.infrastructure.PedidoVentaTiempoMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("pedidoVentaTiempoService")
public class PedidoVentaTiempoSvc extends BasicSvc<PedidoVentaTiempoDTO, PedidoVentaTiempoFilterDTO> {

	private final PedidoVentaTiempoMapper pedidoVentaTiempoMapper;

	public PedidoVentaTiempoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy PedidoVentaTiempoMapper pedidoVentaTiempoMapper) {
		super(usuarioSesionService);
		this.pedidoVentaTiempoMapper = pedidoVentaTiempoMapper;
	}

	@Override
	public PedidoVentaTiempoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. PedidoVentaTiempo");
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
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaTiempoDTO actualizar(PedidoVentaTiempoDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaTiempoDTO inactivar(PedidoVentaTiempoDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
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
	public List<PedidoVentaTiempoDTO> listarConsulta(PedidoVentaTiempoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaTiempoDTO guardar(PedidoVentaTiempoDTO dto, String token) throws ServerException {
		return super.guardar(dto, token);
	}


}