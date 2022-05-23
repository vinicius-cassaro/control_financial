package control.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import control.security.model.dto.LoginDTO;
import control.security.model.dto.TokenDTO;
import control.security.service.LogoutServiceImpl;
import control.security.service.TokenServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/auth")
public class AuthenticationController{
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private TokenServiceImpl tokenService;
	
	@Autowired
	private LogoutServiceImpl logOutService;
	
	@PostMapping("/login")
	public ResponseEntity<TokenDTO> auth(@RequestBody @Validated LoginDTO loginDTO){
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());
		
		Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
		
		String token = tokenService.generateToken(authentication);
		
		return ResponseEntity.ok(TokenDTO.builder().type("Bearer").token(token).build());
		
	}
	
	@PostMapping("/logout")
	@Operation(summary = "Logout", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<String> logout() {return logOutService.logout(tokenService.getToken(request)).isPresent() ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();}
}