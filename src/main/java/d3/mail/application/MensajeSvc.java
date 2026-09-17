package d3.mail.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.mail.domain.MensajeDTO;
import d3.mail.domain.MensajeFilterDTO;
import d3.mail.infrastructure.MensajeMapper;
import d3.shared.application.BasicSvc;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import jakarta.annotation.PostConstruct;

@Service("mensajeService")
public class MensajeSvc extends BasicSvc<MensajeDTO, MensajeFilterDTO> {

	private final MensajeMapper mensajeMapper;

	public MensajeSvc(@Lazy MensajeMapper mensajeMapper) {
		this.mensajeMapper = mensajeMapper;
	}

	@Override
	public MensajeDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Mensaje");
		MensajeFilterDTO dto = new MensajeFilterDTO();
		dto.setLlaveTabla(llave);
		return mensajeMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = mensajeMapper;
	}

	@Override
	public MensajeDTO activar(MensajeDTO dto) throws ServerException {
		return super.activar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO actualizar(MensajeDTO dto) throws ServerException {
		return super.actualizar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO inactivar(MensajeDTO dto) throws ServerException {
		return super.inactivar(dto);
	}

	@Override
	public MensajeDTO consultaUnica(MensajeFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(MensajeFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<MensajeDTO> listarConsulta(MensajeFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	public List<MensajeDTO> mensajesUsuario(MensajeFilterDTO dto) throws ServerException {
		if (dto.getUsuario() == null)
			throw new ServerException("Identifique el usuario");
		paginar(dto);
		return mensajeMapper.mensajesUsuario(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO guardar(MensajeDTO dto) throws ServerException {
		return super.guardar(dto);
	}

	public List<MensajeDTO> correosMensaje(String estado, String documento, String modificador)
			throws ServerException {
		return mensajeMapper.correosMensaje(estado, documento, modificador, SessionContext.getCurrentToken());
	}


}