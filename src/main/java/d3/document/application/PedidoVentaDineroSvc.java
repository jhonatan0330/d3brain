package d3.document.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.binding.BindingException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.document.domain.PedidoVentaDTO;
import d3.document.domain.PedidoVentaDineroDTO;
import d3.document.domain.PedidoVentaDineroFilterDTO;
import d3.document.infrastructure.PedidoVentaDineroMapper;
import d3.shared.application.BasicSvc;
import d3.shared.domain.ServerException;
import jakarta.annotation.PostConstruct;

@Service("pedidoVentaDineroService")
public class PedidoVentaDineroSvc extends BasicSvc<PedidoVentaDineroDTO, PedidoVentaDineroFilterDTO> {

	private final PedidoVentaDineroMapper pedidoVentaDineroMapper;

	public PedidoVentaDineroSvc(
			@Lazy PedidoVentaDineroMapper pedidoVentaDineroMapper) {
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
	public PedidoVentaDineroDTO actualizar(PedidoVentaDineroDTO dto) throws ServerException {
		throw new ServerException("Metodo inactivo usar guardar");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDineroDTO inactivar(PedidoVentaDineroDTO dto) throws ServerException {
		throw new ServerException("Metodo inactivo usar inactivar ConHistorial");
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDineroDTO guardar(PedidoVentaDineroDTO dto) throws ServerException {
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

	public List<PedidoVentaDineroDTO> listar2DocumentoVisible(List<PedidoVentaDTO> documentos) {
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
		}
		dto.setLlaveTabla(generarLlave());
		try {
			pedidoVentaDineroMapper.insertarHistorico(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
		return dto;

	}

	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDineroDTO inactivarConHistorial(PedidoVentaDineroDTO dto, Integer historico) {
		return pedidoVentaDineroMapper.inactivarHistorico(dto.getLlaveTabla(),
				(historico == null) ? null : "Historico");
	}

}