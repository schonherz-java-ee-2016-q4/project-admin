package hu.schonherz.project.admin.service.api.service;

import java.util.List;

import hu.schonherz.project.admin.service.api.vo.TestUserVo;

public interface UserService {

	TestUserVo findByUsername(String username);

	TestUserVo registrationUser(TestUserVo userVo);
	
	List<TestUserVo> findAll();
}
