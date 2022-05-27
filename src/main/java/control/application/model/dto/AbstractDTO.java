package control.application.model.dto;

public abstract class AbstractDTO<ID>{

	private ID id;
	
	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}
}
