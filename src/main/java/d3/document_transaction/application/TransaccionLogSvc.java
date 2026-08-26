package d3.document_transaction.application;

import java.util.Date;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.document_transaction.domain.DocumentoTransaccionDTO;
import d3.document_transaction.domain.TransaccionLogDTO;
import d3.document_transaction.domain.TransaccionLogFilterDTO;
import d3.document_transaction.infrastructure.TransaccionLogMapper;
import d3.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("transaccionLogService")
public class TransaccionLogSvc extends BasicSvc<TransaccionLogDTO, TransaccionLogFilterDTO> {

	private final TransaccionLogMapper transaccionLogMapper;

	public TransaccionLogSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy TransaccionLogMapper transaccionLogMapper) {
		super(usuarioSesionService);
		this.transaccionLogMapper = transaccionLogMapper;
	}

	@Override
	public TransaccionLogDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. TransaccionLog");
		TransaccionLogFilterDTO dto = new TransaccionLogFilterDTO();
		dto.setLlaveTabla(llave);
		return transaccionLogMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = transaccionLogMapper;
	}

	public TransaccionLogDTO finalizar(Date startDate, String transactionId, String session) throws ServerException {
		TransaccionLogDTO newLog = new TransaccionLogDTO();
		newLog.setFechaInicio(startDate);
		newLog.setFechaFin(new Date());
		newLog.setTransaccion(transactionId);
		newLog.setSesion(session);
		return saveSimple(newLog);
	}

	public TransaccionLogDTO endToAPI(DocumentoTransaccionDTO pTransaction, String pInput, String pOutput)
			throws ServerException {
		TransaccionLogDTO newLog = new TransaccionLogDTO();
		newLog.setFechaInicio(pTransaction.getFecha());
		newLog.setFechaFin(new Date());
		newLog.setTransaccion(pTransaction.getLlaveTabla());
		// newLog.setSesion(session);
		newLog.setUsuario(pTransaction.getUsuario());
		newLog.setEntrada(pInput);
		newLog.setSalida(pOutput);
		return saveSimple(newLog);
	}

}