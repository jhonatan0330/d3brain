package d3.document_execution.application;

import java.util.List;

import java.util.ArrayList;
import java.util.Date;

import org.apache.ibatis.binding.BindingException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.document_execution.domain.PedidoVentaDineroDTO;
import d3.document_execution.domain.PedidoVentaDineroFilterDTO;
import d3.document_execution.infrastructure.PedidoVentaDineroMapper;
import d3.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("pedidoVentaDineroService")
public class PedidoVentaDineroSvc extends BasicSvc<PedidoVentaDineroDTO, PedidoVentaDineroFilterDTO> {

	private final PedidoVentaDineroMapper pedidoVentaDineroMapper;

	public PedidoVentaDineroSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy PedidoVentaDineroMapper pedidoVentaDineroMapper) {
		super(usuarioSesionService);
		this.pedidoVentaDineroMapper = pedidoVentaDineroMapper;
	}

	@Override
	public PedidoVentaDineroDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. PedidoVentaDinero");
		PedidoVentaDineroFilterDTO dto = new PedidoVentaDineroFilterDTO();
		dto.setLlaveTabla(llave);
		return pedidoVentaDineroMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = pedidoVentaDineroMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDineroDTO actualizar(PedidoVentaDineroDTO dto, String token) throws ServerException {
		throw new ServerException("Metodo inactivo usar guardar");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDineroDTO inactivar(PedidoVentaDineroDTO dto, String token) throws ServerException {
		throw new ServerException("Metodo inactivo usar inactivar ConHistorial");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDineroDTO guardar(PedidoVentaDineroDTO dto, String token) throws ServerException {
		throw new ServerException("Metodo inactivo usar guardar con historial");
	}

	public PedidoVentaDineroDTO consultaPorDocumento(String documento, Integer historico, String name)
			throws ServerException {
		try {
			return pedidoVentaDineroMapper.consultaPorDocumento(documento, historico, generarLlave());
		} catch (BindingException ex) {
			throw new ServerException(
					"El documento " + name + " tiene la siguiente novedad al consultar el valor : " + ex.getMessage());
		} catch (MyBatisSystemException msex) {
			throw new ServerException("El documento " + name + " tiene la siguiente novedad al consultar el valor : "
					+ msex.getCause().getMessage());
		} catch (Exception e) {
			throw new ServerException(
					"El documento " + name + " tiene la siguiente novedad al consultar el valor : " + e.getMessage());
		}
	}

	public List<PedidoVentaDineroDTO> listar2DocumentoVisible(List<PedidoVentaDTO> documentos) throws ServerException {// La
																														// plantilla
																														// es
																														// para
																														// optimizar
																														// la
																														// consultas
																														// de
																														// la
																														// particion
		if (documentos == null || documentos.isEmpty())
			return null;
		List<PedidoVentaDTO> produccion = null;
		List<PedidoVentaDTO> historicos = null;
		for (PedidoVentaDTO iDocumento : documentos) {
			if (iDocumento.getHistorico() == null) {
				if (produccion == null)
					produccion = new ArrayList<PedidoVentaDTO>();
				produccion.add(iDocumento);
			} else {
				if (historicos == null)
					historicos = new ArrayList<PedidoVentaDTO>();
				historicos.add(iDocumento);
			}
		}
		return pedidoVentaDineroMapper.listar2DocumentoVisible(produccion, historicos);
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDineroDTO guardarConHistorial(PedidoVentaDineroDTO dto, Integer historico)
			throws ServerException {
		dto.setFecha(new Date());
		if (historico == null) {
			return save(dto);
		} else {
			dto.setLlaveTabla(generarLlave());
			try {
				pedidoVentaDineroMapper.insertarHistorico(dto);
			} catch (Exception e) {
				throw new ServerException(e.getCause().getMessage());
			}
			return dto;
		}
	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDineroDTO inactivarConHistorial(PedidoVentaDineroDTO dto, Integer historico)
			throws ServerException {
		return pedidoVentaDineroMapper.inactivarHistorico(dto.getLlaveTabla(),
				(historico == null) ? null : "Historico");
	}

}