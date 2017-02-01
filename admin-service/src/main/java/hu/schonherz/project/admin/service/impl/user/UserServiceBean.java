package hu.schonherz.project.admin.service.impl.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.interceptor.Interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.project.admin.data.entity.UserEntity;
import hu.schonherz.project.admin.data.repository.UserRepository;
import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.service.mapper.user.UserVoMapper;

@Stateless(mappedName = "UserService")
@Local(UserServiceLocal.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class UserServiceBean implements UserServiceLocal {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceBean.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserVo findByUsername(final String username) {
        UserEntity user = userRepository.findByUsername(username);
        return UserVoMapper.toVo(user);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public UserVo registrationUser(final UserVo userVo) {
        try {
            UserEntity user = UserVoMapper.toEntity(userVo);
            user = userRepository.save(user);
            return UserVoMapper.toVo(user);
        } catch (Throwable t) {
            LOG.error("----- The exception was caught in the service layer! -----");
            return null;
        }
    }

    @Override
    public List<UserVo> findAll() {
        List<UserEntity> allEntities = userRepository.findAll();
        return allEntities.stream().map(entity -> UserVoMapper.toVo(entity)).collect(Collectors.toList());
    }

}
