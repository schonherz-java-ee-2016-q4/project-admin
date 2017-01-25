package hu.schonherz.project.admin.service.api.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestUserVo extends TestBaseVo implements Serializable{
	
	private static final long serialVersionUID = 5534955718398254750L;

	private String username;
	private String password;
	private String email;

}
