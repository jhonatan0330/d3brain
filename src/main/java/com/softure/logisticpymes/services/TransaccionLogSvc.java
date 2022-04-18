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
import com.softure.logisticpymes.dto.TransaccionLogDTO;
import com.softure.logisticpymes.dto.filter.TransaccionLogFilterDTO;
import com.softure.logisticpymes.persistence.TransaccionLogMapper;

@Service("transaccionLogService")
public class TransaccionLogSvc extends BasicSvc<TransaccionLogDTO, TransaccionLogFilterDTO> {
	
	@Autowired
	private TransaccionLogMapper transaccionLogMapper;
	
	// BEGIN region servicesTransaccionLog
	// END region servicesTransaccionLog

	@Override
	public TransaccionLogDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. TransaccionLog");
		TransaccionLogFilterDTO dto = new TransaccionLogFilterDTO();
		dto.setLlaveTabla(llave);
		return transaccionLogMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = transaccionLogMapper;
	}
	
	@Override
	public TransaccionLogDTO activar(TransaccionLogDTO dto, String token) throws ServerException {
		// BEGIN TransaccionLog_activar
		return super.activar(dto, token);
		// END TransaccionLog_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TransaccionLogDTO actualizar( TransaccionLogDTO dto, String token) throws ServerException {
		// BEGIN TransaccionLog_actualizar
		return super.actualizar(dto, token);
		// END TransaccionLog_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TransaccionLogDTO inactivar(TransaccionLogDTO dto, String token) throws ServerException {
		// BEGIN TransaccionLog_inactivar
		return super.inactivar(dto, token);
		// END TransaccionLog_inactivar
	}
	
	@Override
	public TransaccionLogDTO consultaUnica(TransaccionLogFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(TransaccionLogFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<TransaccionLogDTO> listarConsulta(TransaccionLogFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TransaccionLogDTO guardar(TransaccionLogDTO dto, String token) throws ServerException {
		// BEGIN TransaccionLog_guardar
		return super.guardar(dto, token);
		// END TransaccionLog_guardar
	}

// BEGIN region aditionalMethods
	public TransaccionLogDTO finalizar(Date startDate, String transactionId) throws ServerException {
		TransaccionLogDTO newLog = new TransaccionLogDTO();
		newLog.setFechaInicio(startDate);
		newLog.setFechaFin(new Date());
		newLog.setTransaccion(transactionId);
		return save(newLog);
	}
// END region aditionalMethods

}