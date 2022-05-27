package control.application.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.ParamDef;

import control.security.model.UserModel;

@Entity(name = "tags")
@FilterDefs({
    @FilterDef(name = "userId", parameters = { @ParamDef(name = "user_id", type = "long") })
})
@Filters({
    @Filter(name = "userId", condition = "user_id = :user_id")
})
public class TagModel extends AbstractModel<Long>{

	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(nullable = false, length = 200)
	private String description;
	
	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id")
	private UserModel userModel;

	public TagModel() {
		super();
	}
	
	public TagModel(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

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
