package com.softure.document_transaction.application;

import java.util.Date;

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
import org.springframework.context.annotation.Lazy;
import com.softure.authentication.application.UsuarioSesionSvc;

@Service("transaccionErrorService")
public class TransaccionErrorSvc extends BasicSvc<TransaccionErrorDTO, TransaccionErrorFilterDTO> {

	private final TransaccionErrorMapper transaccionErrorMapper;
	private final UploadSvc uploadService;

	public TransaccionErrorSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy TransaccionErrorMapper transaccionErrorMapper, @Lazy UploadSvc uploadService) {
		super(usuarioSesionService);
		this.transaccionErrorMapper = transaccionErrorMapper;
		this.uploadService = uploadService;
	}

	@Override
	public TransaccionErrorDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. TransaccionError");
		TransaccionErrorFilterDTO dto = new TransaccionErrorFilterDTO();
		dto.setLlaveTabla(llave);
		return transaccionErrorMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = transaccionErrorMapper;
	}

	@Transactional(value = "transactionManager", propagation = Propagation.NOT_SUPPORTED)
	public TransaccionErrorDTO finalizar(Date startDate, String error, String userId, String dto, String token)
			throws ServerException {
		TransaccionErrorDTO newLog = new TransaccionErrorDTO();
		newLog.setFechaInicio(startDate);
		newLog.setFechaFin(new Date());
		newLog.setError(error);
		newLog.setUsuario(userId);
		if (dto != null) {
			try {
				newLog.setEntrada(uploadService.uploadFile(dto.getBytes(), "Parameter.txt", token, "logs", "private"));
			} catch (Exception e) {

			}
		}
		return saveSimple(newLog);
	}

}