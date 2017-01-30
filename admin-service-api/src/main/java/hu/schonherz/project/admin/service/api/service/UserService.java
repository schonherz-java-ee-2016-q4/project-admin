package hu.schonherz.project.admin.service.api.service;

import java.util.List;

import hu.schonherz.project.admin.service.api.vo.UserVo;

public interface UserService {

	UserVo findByUsername(String username);

	UserVo registrationUser(UserVo userVo);
	
	List<UserVo> findAll();
}
