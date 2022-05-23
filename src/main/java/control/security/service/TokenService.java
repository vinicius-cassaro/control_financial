package control.security.service;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public interface TokenService {
	
	public String generateToken(Authentication authentication);
	
	public String getToken(HttpServletRequest request);
	
	public boolean isTokenValid(String token);
	
	public Long getTokenId(String token);
	
	public Date getTokenExpiration(String token);
}
