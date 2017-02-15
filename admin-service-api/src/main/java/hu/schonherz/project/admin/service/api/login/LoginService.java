package hu.schonherz.project.admin.service.api.login;

import hu.schonherz.project.admin.service.api.vo.LoginVo;
import java.time.LocalDateTime;
import java.util.Collection;

public interface LoginService {

    Collection<LoginVo> findAll();

    LoginVo findById(Long id);

    Collection<LoginVo> findByUserId(Long userId);

    Collection<LoginVo> findByDate(LocalDateTime loginDate);

    Long save(LoginVo login);
}
