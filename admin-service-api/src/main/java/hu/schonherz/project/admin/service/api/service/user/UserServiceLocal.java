package hu.schonherz.project.admin.service.api.service.user;

import hu.schonherz.project.admin.service.api.vo.UserVo;

public interface UserServiceLocal extends UserService {

    UserVo registrationUser(UserVo userVo);

}
