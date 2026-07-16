package com.softure.upload.application;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.upload.domain.CargaArchivoDTO;
import com.softure.upload.domain.CargaArchivoFilterDTO;
import com.softure.upload.infrastructure.CargaArchivoMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("cargaArchivoService")
public class CargaArchivoSvc extends BasicSvc<CargaArchivoDTO, CargaArchivoFilterDTO> {

	private final CargaArchivoMapper cargaArchivoMapper;

	public CargaArchivoSvc(@Lazy UsuarioSesionSvc usuarioSesionService, @Lazy CargaArchivoMapper cargaArchivoMapper) {
		super(usuarioSesionService);
		this.cargaArchivoMapper = cargaArchivoMapper;
	}

	@Override
	public CargaArchivoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. CargaArchivo");
		CargaArchivoFilterDTO dto = new CargaArchivoFilterDTO();
		dto.setLlaveTabla(llave);
		return cargaArchivoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = cargaArchivoMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public CargaArchivoDTO guardar(CargaArchivoDTO dto, String token) throws ServerException {
		dto.setFechaFin(new Date());
		return super.saveSimple(dto);
	}

}