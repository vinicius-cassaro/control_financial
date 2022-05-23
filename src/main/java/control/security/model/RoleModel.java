package control.security.model;

import javax.persistence.Entity;

import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

@Entity(name = "roles")
@Audited
@AuditTable(value = "roles_audit")
public class RoleModel extends AbstractModel<Long> implements GrantedAuthority{
	

	private static final long serialVersionUID = 1L;
	
	private String name;

	@Override
	public String getAuthority() {
		return this.name;
	}

}