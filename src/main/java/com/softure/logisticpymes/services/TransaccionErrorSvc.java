package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.TransaccionErrorDTO;
import com.softure.logisticpymes.domain.filter.TransaccionErrorFilterDTO;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.TransaccionErrorMapper;

@Service("transaccionErrorService")
public class TransaccionErrorSvc extends BasicSvc<TransaccionErrorDTO, TransaccionErrorFilterDTO> {
	
	@Autowired
	private TransaccionErrorMapper transaccionErrorMapper;
	
	// BEGIN region servicesTransaccionError
	// END region servicesTransaccionError

	@Override
	public TransaccionErrorDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. TransaccionError");
		TransaccionErrorFilterDTO dto = new TransaccionErrorFilterDTO();
		dto.setLlaveTabla(llave);
		return transaccionErrorMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = transaccionErrorMapper;
	}
	
	@Override
	public TransaccionErrorDTO activar(TransaccionErrorDTO dto, String token) throws ServerException {
		// BEGIN TransaccionError_activar
		return super.activar(dto, token);
		// END TransaccionError_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TransaccionErrorDTO actualizar( TransaccionErrorDTO dto, String token) throws ServerException {
		// BEGIN TransaccionError_actualizar
		return super.actualizar(dto, token);
		// END TransaccionError_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TransaccionErrorDTO inactivar(TransaccionErrorDTO dto, String token) throws ServerException {
		// BEGIN TransaccionError_inactivar
		return super.inactivar(dto, token);
		// END TransaccionError_inactivar
	}
	
	@Override
	public TransaccionErrorDTO consultaUnica(TransaccionErrorFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(TransaccionErrorFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<TransaccionErrorDTO> listarConsulta(TransaccionErrorFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TransaccionErrorDTO guardar(TransaccionErrorDTO dto, String token) throws ServerException {
		// BEGIN TransaccionError_guardar
		return super.guardar(dto, token);
		// END TransaccionError_guardar
	}

// BEGIN region aditionalMethods
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public TransaccionErrorDTO finalizar(Date startDate, String error, String userId) throws ServerException {
		TransaccionErrorDTO newLog = new TransaccionErrorDTO();
		newLog.setFechaInicio(startDate);
		newLog.setFechaFin(new Date());
		newLog.setError(error);
		newLog.setUsuario(userId);
		return save(newLog);
	}
// END region aditionalMethods

}