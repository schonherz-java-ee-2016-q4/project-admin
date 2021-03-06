package hu.schonherz.project.admin.service.api.login;

import hu.schonherz.project.admin.service.api.vo.LoginVo;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface LoginService {

    Collection<LoginVo> findAll();

    LoginVo findById(Long id);

    List<LoginVo> findByUserId(Long userId);

    List<LoginVo> findByUserIdOrderByLoginDateDesc(Long userId);

    Collection<LoginVo> findByDate(LocalDateTime loginDate);

    Long save(LoginVo login);
}
