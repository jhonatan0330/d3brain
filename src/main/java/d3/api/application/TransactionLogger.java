package d3.api.application;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import d3.shared.domain.ServerException;
import d3.document_transaction.application.DocumentoTransaccionSvc;
import d3.document_transaction.application.TransaccionErrorSvc;
import d3.document_transaction.application.TransaccionLogSvc;
import d3.document_transaction.domain.DocumentoTransaccionDTO;
import d3.upload.application.UploadSvc;
import org.springframework.context.annotation.Lazy;

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

	public String toJson(Object obj, String pToken) throws ServerException {
		try {
			return uploadService.uploadFile(writer.writeValueAsString(obj).getBytes(), "Parameter.txt", pToken,
					"api_external", "private");
		} catch (JsonProcessingException e) {
			return "{}";
		}
	}

	public <T> T executeWithLogging(String pToken, Object input, TransactionOperation<T> operation)
			throws ServerException {

		DocumentoTransaccionDTO tran = transaccionSvc.crear(pToken);
		String inputJson = toJson(input, pToken);
		try {
			T result = operation.run();
			String outputJson = toJson(result, pToken);
			logSvc.endToAPI(tran, inputJson, outputJson);
			return result;
		} catch (Exception e) {
			errorSvc.finalizar(tran.getFecha(), e.getMessage(), tran.getUsuario(), inputJson, pToken);
			throw new ServerException(e.getMessage(), false);
		}
	}

	@FunctionalInterface
	public interface TransactionOperation<T> {
		T run() throws Exception;
	}
}
