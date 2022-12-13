package com.softure.document_transition.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;

import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.domain.ProcesoEstadoDTO;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_transition.domain.PedidoVentaAjusteDTO;
import com.softure.document_transition.domain.PedidoVentaAjusteFilterDTO;
import com.softure.document_transition.infrastructure.PedidoVentaAjusteMapper;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;

@Service("pedidoVentaAjusteService")
public class PedidoVentaAjusteSvc extends BasicSvc<PedidoVentaAjusteDTO, PedidoVentaAjusteFilterDTO> {
	
	@Autowired
	private PedidoVentaAjusteMapper pedidoVentaAjusteMapper;
	
	// BEGIN region servicesPedidoVentaAjuste
	@Autowired private PedidoVentaSvc documentoService;
	@Autowired private ProcesoEstadoSvc procesoEstadoService;
	@Autowired private CallManageTransition manageTransitionFunction;
	// END region servicesPedidoVentaAjuste

	@Override
	public PedidoVentaAjusteDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PedidoVentaAjuste");
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
		// BEGIN PedidoVentaAjuste_activar
		throw new ServerException("Metodo sin implementar");
		// END PedidoVentaAjuste_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaAjusteDTO actualizar( PedidoVentaAjusteDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaAjuste_actualizar
		throw new ServerException("Metodo sin implementar");
		// END PedidoVentaAjuste_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaAjusteDTO inactivar(PedidoVentaAjusteDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaAjuste_inactivar
		throw new ServerException("Metodo sin implementar");
		// END PedidoVentaAjuste_inactivar
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
	public List<PedidoVentaAjusteDTO> listarConsulta(PedidoVentaAjusteFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaAjusteDTO guardar(PedidoVentaAjusteDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaAjuste_guardar
		PedidoVentaDTO documento = documentoService.consultaXId(dto.getDocumento());
		dto.setEstadoInicial(documento.getEstadoExpediente());
		ProcesoEstadoDTO estadoInicial = procesoEstadoService.consultaXId(documento.getEstadoExpediente());
		ProcesoEstadoDTO estadoFinal = procesoEstadoService.consultaXId(dto.getEstadoFinal());
		if(estadoFinal.getProceso().compareTo(estadoInicial.getProceso())!=0) throw new ServerException("El estado no pertenece al mismo proceso");
		dto.setFecha(new Date());
		dto.setResponsable(getUserFlex(token));
		dto = super.guardar(dto, token);
		documento.setEstadoExpediente(estadoFinal.getLlaveTabla());
		documento.setEstado(estadoFinal.getEstadoDocumento());
		documentoService.update(documento);
		manageTransitionFunction.assignResponsibleToActivity(documento.getLlaveTabla(), estadoFinal.getLlaveTabla(), estadoFinal.getNombre(), null, token);
		//Queda pendiente lo del responsable
		return dto;
		// END PedidoVentaAjuste_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}