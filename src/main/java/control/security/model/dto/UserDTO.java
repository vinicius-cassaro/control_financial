package control.security.model.dto;

import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import control.security.model.RoleModel;

@SuppressWarnings("serial")
public class UserDTO extends AbstractDTO<Long> implements UserDetails{

	private String userName;
	
	private String email;
	
	private String password;
	
	private Boolean isEnable;
	
	private Set<RoleModel> roles;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public Collection<RoleModel> getAuthorities() {
		return null;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return isEnable;
	}

	public void setIsEnable(Boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	public Set<RoleModel> getRoles() {
		return roles;
	}

	public void setRoles(Set<RoleModel> roles) {
		this.roles = roles;
	}
}
