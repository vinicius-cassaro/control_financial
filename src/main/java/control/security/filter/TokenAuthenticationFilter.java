package control.security.filter;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import control.security.model.UserModel;
import control.security.repository.UserRepository;
import control.security.service.LogoutServiceImpl;
import control.security.service.TokenServiceImpl;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private TokenServiceImpl tokenService;
	private UserRepository repository;
	private LogoutServiceImpl logOutService;

	@Autowired
	public TokenAuthenticationFilter(TokenServiceImpl tokenService, UserRepository repository,
			LogoutServiceImpl logOutService) {
		this.tokenService = tokenService;
		this.repository = repository;
		this.logOutService = logOutService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String tokenFromHeader = getTokenFromHeader(request);
		boolean tokenValid;
		if (tokenFromHeader != null) {
			var tokenBlackList = logOutService.findByToken(tokenFromHeader);
			if (tokenBlackList.isPresent()) {
				tokenValid = false;
			} else {
				tokenValid = tokenService.isTokenValid(tokenFromHeader);
			}
			if (tokenValid) {
				this.authenticate(tokenFromHeader);
			}
		} else {
			tokenValid = tokenService.isTokenValid(tokenFromHeader);
			if(tokenValid) {
				this.authenticate(tokenFromHeader);
			}
		}

		filterChain.doFilter(request, response);
	}

	private void authenticate(String tokenFromHeader) {
		Long id = tokenService.getTokenId(tokenFromHeader);

		Optional<UserModel> optionalUser = repository.findById(id);

		if (optionalUser.isPresent()) {

			UserModel user = optionalUser.get();
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					user, null, user.getRoles());
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}
	}

	private String getTokenFromHeader(HttpServletRequest request) {
		return tokenService.getToken(request);
	}
}