package d3.document.application;

import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.document.domain.TransaccionErrorDTO;
import d3.document.domain.TransaccionErrorFilterDTO;
import d3.document.infrastructure.TransaccionErrorMapper;
import d3.shared.application.BasicSvc;
import d3.upload.application.UploadSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

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