package d3.api.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import d3.document.application.DocumentoTransaccionSvc;
import d3.document.application.TransaccionErrorSvc;
import d3.document.application.TransaccionLogSvc;
import d3.document.domain.DocumentoTransaccionDTO;
import d3.shared.domain.ServerException;
import d3.upload.application.UploadSvc;

@Component
public class TransactionLogger {

	private final TransaccionLogSvc logSvc;
	private final TransaccionErrorSvc errorSvc;
	private final DocumentoTransaccionSvc transaccionSvc;
	private final UploadSvc uploadService;
	private final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

	public TransactionLogger(@Lazy TransaccionLogSvc logSvc, @Lazy TransaccionErrorSvc errorSvc,
			@Lazy DocumentoTransaccionSvc transaccionSvc, @Lazy UploadSvc uploadService) {
		this.logSvc = logSvc;
		this.errorSvc = errorSvc;
		this.transaccionSvc = transaccionSvc;
		this.uploadService = uploadService;
	}

	public String toJson(Object obj) throws ServerException {
		try {
			return uploadService.uploadFile(writer.writeValueAsString(obj).getBytes(), "Parameter.txt", "api_external",
					"private");
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}

	public <T> T executeWithLogging(Object input, TransactionOperation<T> operation) throws ServerException {

		DocumentoTransaccionDTO tran = transaccionSvc.crear();
		String inputJson = toJson(input);
		try {
			T result = operation.run();
			String outputJson = toJson(result);
			logSvc.endToAPI(tran, inputJson, outputJson);
			return result;
		} catch (Exception e) {
			errorSvc.finalizar(tran.getFecha(), e.getMessage(), tran.getUsuario(), inputJson);
			throw new ServerException(e.getMessage(), false);
		}
	}

	@FunctionalInterface
	public interface TransactionOperation<T> {
		T run() throws Exception;
	}
}
