package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;

public class TestUserVo extends TestBaseVo implements Serializable{
	
	private static final long serialVersionUID = 5534955718398254750L;

	private String username;

	private String password;
	
	private String email;

	public TestUserVo() {
		super();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
