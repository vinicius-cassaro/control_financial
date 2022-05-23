package control.security.service;

import control.security.model.UserModel;
import control.security.model.dto.UserDTO;

public interface UserService {

	public UserDTO save(UserModel user);
	
	public UserDTO update(Long id, UserModel user, Long idToken) throws Exception;
	
	public void delete(Long id, Long idToken) throws Exception;
}
