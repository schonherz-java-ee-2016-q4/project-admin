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

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.project.admin.data.entity.UserEntity;
import hu.schonherz.project.admin.data.repository.UserRepository;
import hu.schonherz.project.admin.service.api.encrypter.Encrypter;
import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.service.mail.MailSender;
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

    @Override
    public void delete(final Long id) {
        userRepository.delete(id);
    }

    @Override
    public void changeStatus(final Long id) {
        UserEntity userEntity = userRepository.findOne(id);
        userEntity.setActive(!(userEntity.isActive()));
    }

    @Override
    public void resetPassword(final Long id) {
        final int passwordLength = 8;
        UserEntity userEntity = userRepository.findOne(id);
        String generatedPassword = RandomStringUtils.randomAlphanumeric(passwordLength);
        String hashedPassword = Encrypter.encrypt(generatedPassword);
        LOG.info("The generated password is: {}", generatedPassword);
        LOG.info("The hashed password is: {}", hashedPassword);
        userEntity.setPassword(hashedPassword);
        MailSender.sendFromGmail(userEntity.getEmail(), generatedPassword);
    }

    @Override
    public UserVo findById(final Long id) {
        return UserVoMapper.toVo(userRepository.findOne(id));
    }

}
