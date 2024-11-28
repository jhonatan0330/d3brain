package com.softure.document_transaction.application;

import java.util.List;

// BEGIN region interImport
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.softure.document_transaction.domain.TransaccionErrorDTO;
import com.softure.document_transaction.domain.TransaccionErrorFilterDTO;
import com.softure.document_transaction.infrastructure.TransaccionErrorMapper;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.upload.application.UploadSvc;

import jakarta.annotation.PostConstruct;

@Service("transaccionErrorService")
public class TransaccionErrorSvc extends BasicSvc<TransaccionErrorDTO, TransaccionErrorFilterDTO> {
	
	@Autowired @Lazy 
	private TransaccionErrorMapper transaccionErrorMapper;
	
	@Autowired @Lazy 
	private UploadSvc uploadService;

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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TransaccionErrorDTO actualizar( TransaccionErrorDTO dto, String token) throws ServerException {
		// BEGIN TransaccionError_actualizar
		return super.actualizar(dto, token);
		// END TransaccionError_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
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
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TransaccionErrorDTO guardar(TransaccionErrorDTO dto, String token) throws ServerException {
		// BEGIN TransaccionError_guardar
		return super.guardar(dto, token);
		// END TransaccionError_guardar
	}

// BEGIN region aditionalMethods
	@Transactional(value = "transactionManager", propagation = Propagation.NOT_SUPPORTED)
	public TransaccionErrorDTO finalizar(Date startDate, String error, String userId, String dto, String token) throws ServerException {
		TransaccionErrorDTO newLog = new TransaccionErrorDTO();
		newLog.setFechaInicio(startDate);
		newLog.setFechaFin(new Date());
		newLog.setError(error);
		newLog.setUsuario(userId);
		if(dto!=null) {
			try {
				newLog.setEntrada(uploadService.uploadFile(dto.getBytes(), "Parameter.txt", token, "logs"));	
			}catch(Exception e) {
				
			}			
		}
				
		return save(newLog);
	}
// END region aditionalMethods

}