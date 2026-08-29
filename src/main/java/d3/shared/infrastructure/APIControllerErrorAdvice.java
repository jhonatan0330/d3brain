package d3.shared.infrastructure;

import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import d3.shared.application.D3Utils;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedApiErrorResponse;

@ControllerAdvice
public class APIControllerErrorAdvice {

	@ExceptionHandler({ ServerException.class })
	public ResponseEntity<SharedApiErrorResponse> handle(ServerException e) {
		SharedApiErrorResponse response = new SharedApiErrorResponse.ApiErrorResponseBuilder()
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR).withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.withMessage(D3Utils.maskError(e.getTextMessage())).withDetail(e.getOrigen()).build();
		return new ResponseEntity<SharedApiErrorResponse>(response, response.getStatus());
	}

	@ExceptionHandler(PSQLException.class)
	protected ResponseEntity<SharedApiErrorResponse> handleCustomAPIException(PSQLException e) {
		SharedApiErrorResponse response = new SharedApiErrorResponse.ApiErrorResponseBuilder()
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR).withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.withMessage("PSQLException").withDetail(e.getMessage()).build();
		return new ResponseEntity<>(response, response.getStatus());
	}

	@ExceptionHandler(BadSqlGrammarException.class)
	protected ResponseEntity<SharedApiErrorResponse> handleCustomAPIException(BadSqlGrammarException e) {
		SharedApiErrorResponse response = new SharedApiErrorResponse.ApiErrorResponseBuilder()
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR).withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.withMessage("BadSqlGrammarException").withDetail(D3Utils.maskError(e.getCause().getMessage()))
				.build();
		return new ResponseEntity<>(response, response.getStatus());
	}

	@ExceptionHandler(UncategorizedSQLException.class)
	protected ResponseEntity<SharedApiErrorResponse> handleCustomAPIException(UncategorizedSQLException e) {
		SharedApiErrorResponse response = new SharedApiErrorResponse.ApiErrorResponseBuilder()
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR).withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.withMessage("UncategorizedSQLException").withDetail(D3Utils.maskError(e.getMessage())).build();
		return new ResponseEntity<>(response, response.getStatus());
	}

	@ExceptionHandler(NullPointerException.class)
	protected ResponseEntity<SharedApiErrorResponse> handleCustomAPIException(NullPointerException e) {
		SharedApiErrorResponse response = new SharedApiErrorResponse.ApiErrorResponseBuilder()
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR).withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.withMessage("NullPointerException").build();
		return new ResponseEntity<>(response, response.getStatus());
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<SharedApiErrorResponse> handleCustomAPIException(Exception e) {
		SharedApiErrorResponse response = new SharedApiErrorResponse.ApiErrorResponseBuilder()
				.withStatus(HttpStatus.INTERNAL_SERVER_ERROR).withError_code(HttpStatus.INTERNAL_SERVER_ERROR.name())
				.withMessage(e.getMessage()).withDetail(e.getStackTrace()[0].toString()).build();
		return new ResponseEntity<>(response, response.getStatus());
	}
}
