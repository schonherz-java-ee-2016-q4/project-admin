package hu.schonherz.project.admin.service.api.service.user;

import java.util.List;

import hu.schonherz.project.admin.service.api.vo.UserVo;
import java.util.Collection;
import java.util.Set;

public interface UserService {

    UserVo findById(Long id);

    UserVo findByUsername(String username);

    UserVo findByEmail(String email);

    Set<UserVo> findByCompanyName(String companyName);

    List<UserVo> findAll();

    void delete(Long id);

    void changeStatus(Long id);

    void resetPassword(Long id);

    void changeAvailability(Long id, boolean available);

    void saveAll(Collection<UserVo> users);
}
