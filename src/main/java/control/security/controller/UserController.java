package control.security.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import control.security.model.UserModel;
import control.security.model.dto.UserDTO;
import control.security.service.TokenServiceImpl;
import control.security.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;
	private final TokenServiceImpl tokenService;
	
	@Autowired
	private HttpServletRequest request;
	
	public UserController(UserService userService, TokenServiceImpl tokenService) {
		this.userService = userService;
		this.tokenService = tokenService;
	}

	@PostMapping(path = "/save")
	public ResponseEntity<UserDTO> saveUser(@RequestBody UserModel user) { return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));}
	
	@PutMapping("/update/{id}")
	@Operation(summary = "Update user", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<UserDTO> updateUser(@PathVariable("id") Long id, @RequestBody UserModel user) throws Exception { return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, user, tokenService.getTokenId(tokenService.getToken(request))));}
	
	@DeleteMapping("/delete/{id}")
	@Operation(summary = "Delete user", security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<UserDTO> deleteUser(@PathVariable("id") Long id) throws Exception { 
		userService.delete(id, tokenService.getTokenId(tokenService.getToken(request)));
		return ResponseEntity.noContent().build();
	}
}
