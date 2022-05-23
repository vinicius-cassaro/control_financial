package control.security.model;

import java.util.Objects;

import javax.persistence.MappedSuperclass;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;


@MappedSuperclass
public abstract class AbstractModel<ID>{
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private ID id;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractModel<?> other = (AbstractModel<?>) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "id=" + id;
	}
	
		
}
