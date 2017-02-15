package hu.schonherz.project.admin.service.impl.login;

import hu.schonherz.project.admin.data.repository.LoginRepository;
import hu.schonherz.project.admin.service.api.login.LoginService;
import hu.schonherz.project.admin.service.api.vo.LoginVo;
import hu.schonherz.project.admin.service.mapper.login.LoginMapper;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

@Stateless(mappedName = "LoginService")
@Local(LoginService.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class LoginBean implements LoginService {

    @Autowired
    private LoginRepository loginRepository;

    @Override
    public Collection<LoginVo> findAll() {
        return LoginMapper.toVo(loginRepository.findAll());
    }

    @Override
    public LoginVo findById(final Long id) {
        return LoginMapper.toVo(loginRepository.findById(id));
    }

    @Override
    public List<LoginVo> findByUserId(final Long userId) {
        return LoginMapper.toVo(loginRepository.findByUserId(userId));
    }

    @Override
    public Collection<LoginVo> findByDate(final LocalDateTime loginDate) {
        return LoginMapper.toVo(loginRepository.findByLoginDate(loginDate));
    }

    @Override
    public Long save(final LoginVo login) {
        return loginRepository.save(LoginMapper.toEntity(login)).getId();
    }
}
