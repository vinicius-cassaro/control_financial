package control.application.service.exceptions;

public class HttpUnauthorizedException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public HttpUnauthorizedException(String message) {
		super(message);
	}

}
