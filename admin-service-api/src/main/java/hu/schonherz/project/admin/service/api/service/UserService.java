package hu.schonherz.project.admin.service.api.service;

import java.util.List;

import hu.schonherz.project.admin.service.api.vo.UserVo;

public interface UserService {

    UserVo findById(Long id);

    UserVo findByUsername(String username);

    List<UserVo> findAll();

    void delete(Long id);

    void changeStatus(Long id);

    void resetPassword(Long id);
}
