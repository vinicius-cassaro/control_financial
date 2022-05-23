package control.security.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import control.security.model.LogOutModel;
import control.security.repository.LogOutRepository;


@Service
public class LogoutServiceImpl implements LogoutService{
	
	private final LogOutRepository logOutRepository;
	private TokenService tokenService;
	
	public LogoutServiceImpl(LogOutRepository logOutRepository, TokenService tokenService) {
		this.logOutRepository = logOutRepository;
		this.tokenService = tokenService;
	}

	@Override
	public Optional<String> logout(String token) {
		var tokenModel = logOutRepository.findByToken(token);
		if(tokenModel.isEmpty()) {
			var tokenSave = new LogOutModel();
			tokenSave.setToken(token);
			//tokenService.getTokenExpiration(token);
			return Optional.of(logOutRepository.save(tokenSave).getToken());
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<LogOutModel> findByToken(String token) {
		return logOutRepository.findByToken(token);
	}
	
	@Scheduled(cron = "0 0 6,18 * * *")
	public void verifyExperationToken() {
		var tokens = logOutRepository.findAll();
		for(LogOutModel token : tokens){
			if(new Date().after(tokenService.getTokenExpiration(token.getToken()))) {
				logOutRepository.deleteByToken(token.getToken());
			}
        }
	}
}
