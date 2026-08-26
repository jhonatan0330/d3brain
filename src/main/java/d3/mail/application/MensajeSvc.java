package d3.mail.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.logisticpymes.application.BasicSvc;
import d3.mail.domain.MensajeDTO;
import d3.mail.domain.MensajeFilterDTO;
import d3.mail.infrastructure.MensajeMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("mensajeService")
public class MensajeSvc extends BasicSvc<MensajeDTO, MensajeFilterDTO> {

	private final MensajeMapper mensajeMapper;

	public MensajeSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy MensajeMapper mensajeMapper) {
		super(usuarioSesionService);
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
	public MensajeDTO activar(MensajeDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO actualizar(MensajeDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO inactivar(MensajeDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
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
	public MensajeDTO guardar(MensajeDTO dto, String token) throws ServerException {
		return super.guardar(dto, token);
	}

	public List<MensajeDTO> correosMensaje(String estado, String documento, String modificador, String token)
			throws ServerException {
		return mensajeMapper.correosMensaje(estado, documento, modificador, token);
	}


}