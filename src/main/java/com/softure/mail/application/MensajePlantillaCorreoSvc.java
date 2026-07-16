package com.softure.mail.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.mail.domain.MensajePlantillaCorreoFilterDTO;
import com.softure.mail.infrastructure.MensajePlantillaCorreoMapper;
import com.softure.property.application.PropiedadSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("mensajePlantillaCorreoService")
public class MensajePlantillaCorreoSvc extends BasicSvc<MensajePlantillaCorreoDTO, MensajePlantillaCorreoFilterDTO> {

	private final MensajePlantillaCorreoMapper mensajePlantillaCorreoMapper;
	private final PropiedadSvc paramService;

	public MensajePlantillaCorreoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy MensajePlantillaCorreoMapper mensajePlantillaCorreoMapper, @Lazy PropiedadSvc paramService) {
		super(usuarioSesionService);
		this.mensajePlantillaCorreoMapper = mensajePlantillaCorreoMapper;
		this.paramService = paramService;
	}

	@Override
	public MensajePlantillaCorreoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. MensajePlantillaCorreo");
		MensajePlantillaCorreoFilterDTO dto = new MensajePlantillaCorreoFilterDTO();
		dto.setLlaveTabla(llave);
		return mensajePlantillaCorreoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = mensajePlantillaCorreoMapper;
	}

	@Override
	public MensajePlantillaCorreoDTO activar(MensajePlantillaCorreoDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajePlantillaCorreoDTO actualizar(MensajePlantillaCorreoDTO dto, String token) throws ServerException {
		paramService.actualizarValorPropiedad(dto.getLlaveTabla(), dto.getNombre());
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajePlantillaCorreoDTO inactivar(MensajePlantillaCorreoDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public MensajePlantillaCorreoDTO consultaUnica(MensajePlantillaCorreoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(MensajePlantillaCorreoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<MensajePlantillaCorreoDTO> listarConsulta(MensajePlantillaCorreoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajePlantillaCorreoDTO guardar(MensajePlantillaCorreoDTO dto, String token) throws ServerException {
		return super.guardar(dto, token);
	}

	public List<MensajePlantillaCorreoDTO> getFullToSynchronize(List<String> process) {
		return mensajePlantillaCorreoMapper.getFullToSynchronize(process);
	}


}