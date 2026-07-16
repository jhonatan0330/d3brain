package com.softure.document_transition.application;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_transition.domain.PedidoVentaAjusteDTO;
import com.softure.document_transition.domain.PedidoVentaAjusteFilterDTO;
import com.softure.document_transition.infrastructure.PedidoVentaAjusteMapper;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.domain.ProcesoEstadoDTO;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("pedidoVentaAjusteService")
public class PedidoVentaAjusteSvc extends BasicSvc<PedidoVentaAjusteDTO, PedidoVentaAjusteFilterDTO> {

	private final PedidoVentaAjusteMapper pedidoVentaAjusteMapper;
	private final PedidoVentaSvc documentoService;
	private final ProcesoEstadoSvc procesoEstadoService;
	private final CallManageTransition manageTransitionFunction;

	public PedidoVentaAjusteSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy PedidoVentaAjusteMapper pedidoVentaAjusteMapper, @Lazy PedidoVentaSvc documentoService,
			@Lazy ProcesoEstadoSvc procesoEstadoService, @Lazy CallManageTransition manageTransitionFunction) {
		super(usuarioSesionService);
		this.pedidoVentaAjusteMapper = pedidoVentaAjusteMapper;
		this.documentoService = documentoService;
		this.procesoEstadoService = procesoEstadoService;
		this.manageTransitionFunction = manageTransitionFunction;
	}

	@Override
	public PedidoVentaAjusteDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. PedidoVentaAjuste");
		PedidoVentaAjusteFilterDTO dto = new PedidoVentaAjusteFilterDTO();
		dto.setLlaveTabla(llave);
		return pedidoVentaAjusteMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = pedidoVentaAjusteMapper;
	}

	@Override
	public PedidoVentaAjusteDTO activar(PedidoVentaAjusteDTO dto, String token) throws ServerException {
		throw new ServerException("Metodo sin implementar");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaAjusteDTO actualizar(PedidoVentaAjusteDTO dto, String token) throws ServerException {
		throw new ServerException("Metodo sin implementar");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaAjusteDTO inactivar(PedidoVentaAjusteDTO dto, String token) throws ServerException {
		throw new ServerException("Metodo sin implementar");
	}

	@Override
	public PedidoVentaAjusteDTO consultaUnica(PedidoVentaAjusteFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(PedidoVentaAjusteFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<PedidoVentaAjusteDTO> listarConsulta(PedidoVentaAjusteFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaAjusteDTO guardar(PedidoVentaAjusteDTO dto, String token) throws ServerException {
		PedidoVentaDTO documento = documentoService.consultaXId(dto.getDocumento());
		if (documento == null)
			throw new ServerException("El documento no existe");
		dto.setEstadoInicial(documento.getEstadoExpediente());
		ProcesoEstadoDTO estadoInicial = procesoEstadoService.consultaXId(documento.getEstadoExpediente());
		if (estadoInicial == null)
			throw new ServerException("El estado inicial del documento no existe");
		ProcesoEstadoDTO estadoFinal = procesoEstadoService.consultaXId(dto.getEstadoFinal());
		if (estadoFinal == null)
			throw new ServerException("El estado final del documento no existe");
		if (estadoFinal.getProceso().compareTo(estadoInicial.getProceso()) != 0)
			throw new ServerException("El estado no pertenece al mismo proceso");
		dto.setFecha(new Date());
		dto.setResponsable(getUserFlex(token));
		dto = super.guardar(dto, token);
		documento.setEstadoExpediente(estadoFinal.getLlaveTabla());
		documento.setEstado(estadoFinal.getEstadoDocumento());
		documentoService.update(documento);
		manageTransitionFunction.assignResponsibleToActivity(documento.getLlaveTabla(), estadoFinal, null, token);
		// Queda pendiente lo del responsable
		return dto;
	}


}