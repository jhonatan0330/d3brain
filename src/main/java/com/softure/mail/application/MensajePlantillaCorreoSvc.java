package com.softure.mail.application;

import java.util.List;

// BEGIN region interImport
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.mail.domain.MensajePlantillaCorreoFilterDTO;
import com.softure.mail.infrastructure.MensajePlantillaCorreoMapper;

@Service("mensajePlantillaCorreoService")
public class MensajePlantillaCorreoSvc extends BasicSvc<MensajePlantillaCorreoDTO, MensajePlantillaCorreoFilterDTO> {
	
	@Autowired
	private MensajePlantillaCorreoMapper mensajePlantillaCorreoMapper;
	
	// BEGIN region servicesMensajePlantillaCorreo
	// END region servicesMensajePlantillaCorreo

	@Override
	public MensajePlantillaCorreoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. MensajePlantillaCorreo");
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
		// BEGIN MensajePlantillaCorreo_activar
		return super.activar(dto, token);
		// END MensajePlantillaCorreo_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public MensajePlantillaCorreoDTO actualizar( MensajePlantillaCorreoDTO dto, String token) throws ServerException {
		// BEGIN MensajePlantillaCorreo_actualizar
		return super.actualizar(dto, token);
		// END MensajePlantillaCorreo_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public MensajePlantillaCorreoDTO inactivar(MensajePlantillaCorreoDTO dto, String token) throws ServerException {
		// BEGIN MensajePlantillaCorreo_inactivar
		return super.inactivar(dto, token);
		// END MensajePlantillaCorreo_inactivar
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
	public List<MensajePlantillaCorreoDTO> listarConsulta(MensajePlantillaCorreoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public MensajePlantillaCorreoDTO guardar(MensajePlantillaCorreoDTO dto, String token) throws ServerException {
		// BEGIN MensajePlantillaCorreo_guardar
		return super.guardar(dto, token);
		// END MensajePlantillaCorreo_guardar
	}

	public List<MensajePlantillaCorreoDTO> getFullToSynchronize() {
		return mensajePlantillaCorreoMapper.getFullToSynchronize();
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}