package com.softure.authentication.application;

import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionFilterDTO;
import com.softure.authentication.infrastructure.UsuarioAutenticacionAutorizacionMapper;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.mail.application.MailRecoverPasswordService;

import jakarta.annotation.PostConstruct;

@Service("usuarioAutenticacionAutorizacionService")
public class UsuarioAutenticacionAutorizacionSvc
		extends BasicSvc<UsuarioAutenticacionAutorizacionDTO, UsuarioAutenticacionAutorizacionFilterDTO> {

	private final UsuarioAutenticacionAutorizacionMapper usuarioAutenticacionAutorizacionMapper;
	private final MailRecoverPasswordService mailRecoverPasswordService;

	public UsuarioAutenticacionAutorizacionSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy UsuarioAutenticacionAutorizacionMapper usuarioAutenticacionAutorizacionMapper,
			@Lazy MailRecoverPasswordService mailRecoverPasswordService) {
		super(usuarioSesionService);
		this.usuarioAutenticacionAutorizacionMapper = usuarioAutenticacionAutorizacionMapper;
		this.mailRecoverPasswordService = mailRecoverPasswordService;
	}

	@Override
	public UsuarioAutenticacionAutorizacionDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. UsuarioAutenticacionAutorizacion");
		UsuarioAutenticacionAutorizacionFilterDTO dto = new UsuarioAutenticacionAutorizacionFilterDTO();
		dto.setLlaveTabla(llave);
		return usuarioAutenticacionAutorizacionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = usuarioAutenticacionAutorizacionMapper;
	}

	@Override
	public UsuarioAutenticacionAutorizacionDTO activar(UsuarioAutenticacionAutorizacionDTO dto, String token)
			throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioAutenticacionAutorizacionDTO actualizar(UsuarioAutenticacionAutorizacionDTO dto, String token)
			throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioAutenticacionAutorizacionDTO inactivar(UsuarioAutenticacionAutorizacionDTO dto, String token)
			throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public UsuarioAutenticacionAutorizacionDTO consultaUnica(UsuarioAutenticacionAutorizacionFilterDTO dto)
			throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(UsuarioAutenticacionAutorizacionFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<UsuarioAutenticacionAutorizacionDTO> listarConsulta(UsuarioAutenticacionAutorizacionFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public UsuarioAutenticacionAutorizacionDTO guardar(UsuarioAutenticacionAutorizacionDTO dto, String token)
			throws ServerException {
		return super.guardar(dto, token);
	}

	public UsuarioAutenticacionAutorizacionDTO makeTokenLink(String usuario, String correo, String ip, String urlServer)
			throws ServerException {
		UsuarioAutenticacionAutorizacionDTO dto = makeToken(usuario, correo, ip);
		mailRecoverPasswordService.callLink(correo, dto.getLlaveTabla(), dto.getCodigo(), urlServer);
		UsuarioAutenticacionAutorizacionDTO _response = new UsuarioAutenticacionAutorizacionDTO();
		// Aqui despues oculto parte del correo
		_response.setCorreo(correo);
		return _response;
	}

	public void makeTokenNumber(String usuario, String correo, String ip, String urlServer) throws ServerException {
		UsuarioAutenticacionAutorizacionDTO dto = makeToken(usuario, correo, ip);
		mailRecoverPasswordService.callNumber(correo, dto.getLlaveTabla(), dto.getCodigo(), urlServer);
	}

	private UsuarioAutenticacionAutorizacionDTO makeToken(String usuario, String correo, String ip)
			throws ServerException {
		UsuarioAutenticacionAutorizacionDTO dto = new UsuarioAutenticacionAutorizacionDTO();
		dto.setCorreo(correo);
		dto.setUsuario(usuario);
		dto.setIpSolicitud(ip);
		dto.setFechaSolicitud(new Date());
		dto.setFechaMaxima(SoftureUtil.agregarMinutos(new Date(), 15));
		dto.setCodigo(String.valueOf(Double.valueOf(Math.random() * 1000000).intValue()));
		return saveSimple(dto);
	}

	public UsuarioAutenticacionAutorizacionDTO validateLink(String llave, String ip) throws ServerException {
		UsuarioAutenticacionAutorizacionDTO dto = consultaXId(llave);
		if (dto == null)
			throw new ServerException("Token de validacion incorrecto");
		if (dto.getFechaMaxima().compareTo(new Date()) < 0)
			throw new ServerException("Se ha vencido el token, por favor genera un nuevo token");
		dto.setIpRedencion(ip);
		dto.setFechaRedencion(new Date());
		return update(dto);
	}

	public void validateToken(String pUser, String pCode, String pIP) throws ServerException {
		if (pUser == null)
			throw new ServerException("No se recibe el usuario");
		if (pCode == null)
			throw new ServerException("No se recibe el codigo");
		UsuarioAutenticacionAutorizacionFilterDTO _filter = new UsuarioAutenticacionAutorizacionFilterDTO();
		_filter.setUsuario(pUser);
		_filter.setCodigo(pCode);
		_filter.setFechaSolicitudMin(SoftureUtil.agregarMinutos(new Date(), -15));
		_filter.setIpSolicitud(pIP);
		_filter.setEstado(SharedConstants.STATE_ACTIVE);
		UsuarioAutenticacionAutorizacionDTO _tokenNumber = consultaUnica(_filter);
		if (_tokenNumber == null)
			throw new ServerException("Token de validacion incorrecto o vencido en 15 minutos");
		_tokenNumber.setFechaRedencion(new Date());
		_filter.setIpRedencion(pIP);
		update(_tokenNumber);
	}

}