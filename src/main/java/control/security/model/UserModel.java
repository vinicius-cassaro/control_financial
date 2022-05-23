package control.security.model;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
@Entity(name = "users")
@Table(indexes = {
		@Index(columnList = "email", name = "idx_email", unique = true)
})
@Audited
@AuditTable(value = "users_audit")
public class UserModel extends AbstractModel<Long> implements UserDetails{

	@Column(name = "username", nullable= false, length = 255)
	private String userName;
	
	@Column(name = "email", nullable= false, length = 255)
	private String email;
	
	@Column(name = "password", nullable= false, length = 255)
	private String password;
	
	@Column(name = "isEnable", nullable= true)
	private Boolean isEnable = Boolean.TRUE;
	
	@Column
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<RoleModel> roles;

	public UserModel() {
	}

	public UserModel(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

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
