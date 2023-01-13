package com.softure.mail.application;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.domain.MensajeFilterDTO;
import com.softure.mail.infrastructure.MensajeMapper;

@Service("mensajeService")
public class MensajeSvc extends BasicSvc<MensajeDTO, MensajeFilterDTO> {

	@Autowired
	private MensajeMapper mensajeMapper;

	// BEGIN region servicesMensaje
	// END region servicesMensaje

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
		// BEGIN Mensaje_activar
		return super.activar(dto, token);
		// END Mensaje_activar
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO actualizar(MensajeDTO dto, String token) throws ServerException {
		// BEGIN Mensaje_actualizar
		return super.actualizar(dto, token);
		// END Mensaje_actualizar
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO inactivar(MensajeDTO dto, String token) throws ServerException {
		// BEGIN Mensaje_inactivar
		return super.inactivar(dto, token);
		// END Mensaje_inactivar
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
		// BEGIN region mensajesUsuario
		if (dto.getUsuario() == null)
			throw new ServerException("Identifique el usuario");
		paginar(dto);
		return mensajeMapper.mensajesUsuario(dto);
		// END region mensajesUsuario
	}

	@Override
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public MensajeDTO guardar(MensajeDTO dto, String token) throws ServerException {
		// BEGIN Mensaje_guardar
		return super.guardar(dto, token);
		// END Mensaje_guardar
	}

// BEGIN region aditionalMethods
	public List<MensajeDTO> correosMensaje(String estado, String documento,String modificador, String token) throws ServerException {
		return mensajeMapper.correosMensaje(estado, documento, modificador, token);
	}
	
	

// END region aditionalMethods

}