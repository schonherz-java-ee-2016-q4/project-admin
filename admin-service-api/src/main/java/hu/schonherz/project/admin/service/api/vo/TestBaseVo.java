package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;

public class TestBaseVo implements Serializable{

	private static final long serialVersionUID = -3097333328363375029L;

	private Long id;

	public TestBaseVo() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
