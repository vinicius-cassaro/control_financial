package control.security.model.mapper;

import control.security.model.dto.UserDTO;
import control.security.model.UserModel;

public class UserMapper {
	
	public static UserDTO convertModelDto(UserModel userModel) {
		var userDto = new UserDTO();
		userDto.setId(userModel.getId());
		userDto.setUserName(userModel.getUserName());
		userDto.setEmail(userModel.getEmail());
		userDto.setPassword(userModel.getPassword());
		userDto.setIsEnable(userModel.isEnabled());
		userDto.setRoles(userModel.getRoles());		
		return userDto;
		
	}
	
	public static UserModel convertDtoModel(UserDTO userDto) {
		var userModel = new UserModel();
		userModel.setUserName(userDto.getUserName());
		userModel.setEmail(userDto.getEmail());
		userModel.setPassword(userDto.getPassword());
		userModel.setIsEnable(userDto.isEnabled());
		userModel.setRoles(userDto.getRoles());		
		return userModel;
		
	}
}
