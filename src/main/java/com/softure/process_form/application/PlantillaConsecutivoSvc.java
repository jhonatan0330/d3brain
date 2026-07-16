package com.softure.process_form.application;

import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;
import com.softure.process_form.domain.PlantillaConsecutivoDTO;
import com.softure.process_form.domain.PlantillaConsecutivoFilterDTO;
import com.softure.process_form.infrastructure.PlantillaConsecutivoMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("plantillaConsecutivoService")
public class PlantillaConsecutivoSvc extends BasicSvc<PlantillaConsecutivoDTO, PlantillaConsecutivoFilterDTO> {

	private final PlantillaConsecutivoMapper plantillaConsecutivoMapper;

	public PlantillaConsecutivoSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy PlantillaConsecutivoMapper plantillaConsecutivoMapper) {
		super(usuarioSesionService);
		this.plantillaConsecutivoMapper = plantillaConsecutivoMapper;
	}

	@Override
	public PlantillaConsecutivoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. PlantillaConsecutivo");
		PlantillaConsecutivoFilterDTO dto = new PlantillaConsecutivoFilterDTO();
		dto.setLlaveTabla(llave);
		return plantillaConsecutivoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = plantillaConsecutivoMapper;
	}

	@Override
	public PlantillaConsecutivoDTO activar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		return super.activar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PlantillaConsecutivoDTO actualizar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		return super.actualizar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PlantillaConsecutivoDTO inactivar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public PlantillaConsecutivoDTO consultaUnica(PlantillaConsecutivoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(PlantillaConsecutivoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<PlantillaConsecutivoDTO> listarConsulta(PlantillaConsecutivoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PlantillaConsecutivoDTO guardar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		PlantillaConsecutivoFilterDTO bdFilter = new PlantillaConsecutivoFilterDTO();
		bdFilter.setCaracteristica(dto.getCaracteristica());
		bdFilter.setValorOpcion(dto.getValorOpcion());
		bdFilter.setEstado(SharedConstants.STATE_ACTIVE);
		PlantillaConsecutivoDTO bd = consultaUnica(bdFilter);
		if (bd != null)
			throw new ServerException("Ya existe una relacion entre caracteristica y opcion");
		return super.guardar(dto, token);
	}


}