package hu.schonherz.project.admin.service.api.service;

import hu.schonherz.project.admin.service.api.vo.UserVo;

public interface UserServiceLocal extends UserService {

    UserVo registrationUser(UserVo userVo);

}
