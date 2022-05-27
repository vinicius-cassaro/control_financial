package control.application.model.dto;

import control.security.model.UserModel;

public class TagDTO extends AbstractDTO<Long>{

	private String name;
	private String description;
	private UserModel userModel;
		
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UserModel getUserModel() {
		return userModel;
	}
	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
}
