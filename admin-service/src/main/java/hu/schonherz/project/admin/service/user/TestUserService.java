package hu.schonherz.project.admin.service.user;

import hu.schonherz.project.admin.service.api.vo.TestUserVo;

public interface TestUserService {
	public TestUserVo findByUsername(String username);
	
	public TestUserVo registrationUser(TestUserVo userVo);
}
