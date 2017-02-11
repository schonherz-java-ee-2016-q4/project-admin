package hu.schonherz.project.admin.service.impl.user;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.*;
import javax.interceptor.Interceptors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.project.admin.data.entity.UserEntity;
import hu.schonherz.project.admin.data.repository.UserRepository;
import hu.schonherz.project.admin.service.api.encrypter.Encrypter;
import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.service.mail.MailSender;
import hu.schonherz.project.admin.service.mapper.user.UserEntityVoMapper;
import lombok.extern.slf4j.Slf4j;

@Stateless(mappedName = "UserService")
@Local(UserServiceLocal.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
@TransactionManagement(TransactionManagementType.CONTAINER)
@Slf4j
public class UserServiceBean implements UserServiceLocal {

    private static final String DOES_NOT_EXIST = " does not exist!";

    @EJB
    private MailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserVo findByUsername(final String username) {
        UserEntity user = userRepository.findByUsername(username);
               return UserEntityVoMapper.toVo(user);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public UserVo registrationUser(final UserVo userVo) {
        UserEntity user = UserEntityVoMapper.toEntity(userVo);
        user = userRepository.save(user);
        if (user == null) {
            log.warn("Failed to persist user " + userVo.getUsername());
        }

        return UserEntityVoMapper.toVo(user);
    }

    @Override
    public List<UserVo> findAll() {
        List<UserEntity> allEntities = userRepository.findAll();
        return allEntities.stream().map(entity -> UserEntityVoMapper.toVo(entity)).collect(Collectors.toList());
    }

    @Override
    public void delete(final Long id) {
        log.warn("User with id " + id + DOES_NOT_EXIST);
        userRepository.delete(id);
    }

    @Override
    public void changeStatus(final Long id) {
        UserEntity userEntity = userRepository.findOne(id);
        if (userEntity != null) {
            userEntity.setActive(!(userEntity.isActive()));
        } else {
            log.warn("User with id " + id + DOES_NOT_EXIST);
        }
    }

    @Override
    public void resetPassword(final Long id) {
        final int passwordLength = 8;
        UserEntity userEntity = findOne(id);
        if (userEntity != null) {
            String generatedPassword = RandomStringUtils.randomAlphanumeric(passwordLength);
            String hashedPassword = Encrypter.encrypt(generatedPassword);
            userEntity.setPassword(hashedPassword);
            mailSender.sendFromGmail(userEntity.getEmail(), generatedPassword);
        }
    }

    @Override
    public UserVo findById(final Long id) {
        UserEntity userEntity = findOne(id);

        return UserEntityVoMapper.toVo(userEntity);
    }

    private UserEntity findOne(final Long id) {
        UserEntity userEntity = userRepository.findOne(id);
        if (userEntity == null) {
            log.warn("User with id " + id + DOES_NOT_EXIST);
        }

        return userEntity;
    }

}
