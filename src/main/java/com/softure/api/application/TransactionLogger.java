package com.softure.api.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.shared.domain.ServerException;
import com.softure.document_transaction.application.DocumentoTransaccionSvc;
import com.softure.document_transaction.application.TransaccionErrorSvc;
import com.softure.document_transaction.application.TransaccionLogSvc;
import com.softure.document_transaction.domain.DocumentoTransaccionDTO;
import com.softure.upload.application.UploadSvc;

@Component
public class TransactionLogger {

    @Autowired @Lazy
    private TransaccionLogSvc logSvc;

    @Autowired @Lazy
    private TransaccionErrorSvc errorSvc;
    
	@Autowired @Lazy
	private DocumentoTransaccionSvc transaccionSvc;
	
	@Autowired @Lazy
	private UploadSvc uploadService;

    private final ObjectWriter writer = new ObjectMapper().writer().withDefaultPrettyPrinter();

    public String toJson(Object obj, String pToken) throws ServerException {
        try {
            return uploadService.uploadFile(writer.writeValueAsString(obj).getBytes(), "Parameter.txt", pToken, "api_external", "private");
        } catch (JsonProcessingException e) {
            return "{}";
        }
    }

    public <T> T executeWithLogging( String pToken, Object input, TransactionOperation<T> operation)
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
