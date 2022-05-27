package control.application.controller.handler;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandlerApplication {

	@ExceptionHandler(value = java.lang.IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse> IllegalArgumentException(HttpServletRequest request, Exception exception) {
		//TODO: testar se a mensagem é nula, se sim estoura outra exception
		var errorResponse = new ErrorResponse(Instant.now(), HttpStatus.BAD_REQUEST.value(), "Illegal argument", exception.getMessage(), request.getRequestURI());
		return ResponseEntity.badRequest().body(errorResponse);
	}
	
	@ExceptionHandler(value = control.security.service.exceptions.EntityNotFoundException.class)
	public ResponseEntity<ErrorResponse> EntityNotFoundException(HttpServletRequest request, Exception exception) {
		//TODO: testar se a mensagem é nula, se sim estoura outra exception
		var errorResponse = new ErrorResponse(Instant.now(), HttpStatus.NOT_FOUND.value(), "Resorce not found", exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}
	
	@ExceptionHandler(value = control.security.service.exceptions.HttpUnauthorizedException.class)
	public ResponseEntity<ErrorResponse> HttpUnauthorizedException(HttpServletRequest request, Exception exception) {
		//TODO: testar se a mensagem é nula, se sim estoura outra exception
		var errorResponse = new ErrorResponse(Instant.now(), HttpStatus.UNAUTHORIZED.value(), "Unauthorized", exception.getMessage(), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
	}
	
	public class ErrorResponse {
		
		private Instant timestamp;
		private Integer statusCode;
		private String error;
		private String message;
		private String path;
		
		public ErrorResponse(Instant timestamp, Integer statusCode, String error, String message, String path) {
			super();
			this.timestamp = timestamp;
			this.statusCode = statusCode;
			this.error = error;
			this.message = message;
			this.path = path;
		}

		public Instant getTimestamp() {
			return timestamp;
		}

		public Integer getStatusCode() {
			return statusCode;
		}

		public String getError() {
			return error;
		}

		public String getMessage() {
			return message;
		}

		public String getPath() {
			return path;
		}		
	}
}
