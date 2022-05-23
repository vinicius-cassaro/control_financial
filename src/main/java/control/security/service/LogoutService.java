package control.security.service;

import java.util.Optional;

import control.security.model.LogOutModel;

public interface LogoutService {
	
	public Optional<LogOutModel> findByToken(String token);

	public Optional<String> logout(String token);
}
