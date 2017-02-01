package hu.schonherz.project.admin.service.api.service;

import hu.schonherz.project.admin.service.api.service.exception.InvalidUserDataException;
import hu.schonherz.project.admin.service.api.vo.UserVo;

public interface UserServiceRemote extends UserService {

    UserVo registrationUser(UserVo userVo) throws InvalidUserDataException;

}
