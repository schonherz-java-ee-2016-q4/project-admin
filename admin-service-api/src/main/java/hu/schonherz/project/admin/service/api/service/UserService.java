package hu.schonherz.project.admin.service.api.service;

import java.util.List;

import hu.schonherz.project.admin.service.api.vo.UserVo;

public interface UserService {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_COMPANY_ADMIN = "COMPANY_ADMIN";
    public static final String ROLE_AGENT = "AGENT";

    UserVo findByUsername(String username);

    List<UserVo> findAll();

    void delete(Long id);

    void changeStatus(Long id);

    void resetPassword(Long id);
}
