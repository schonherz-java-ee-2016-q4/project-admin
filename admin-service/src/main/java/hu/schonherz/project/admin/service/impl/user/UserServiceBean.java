package hu.schonherz.project.admin.service.impl.user;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.project.admin.data.entity.UserEntity;
import hu.schonherz.project.admin.data.repository.UserRepository;
import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.UserVo;
import hu.schonherz.project.admin.service.mapper.user.UserVoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Stateless(mappedName = "UserService")
@Remote(UserServiceRemote.class)
@Local(UserServiceLocal.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class UserServiceBean implements UserServiceLocal, UserServiceRemote {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserVo findByUsername(String username) {
		UserEntity user = userRepository.findByUsername(username);
		return UserVoMapper.toVo(user);
	}

	@Override
	public UserVo registrationUser(UserVo userVo) {
		UserEntity user = UserVoMapper.toEntity(userVo);
		user = userRepository.save(user);
		return UserVoMapper.toVo(user);
	}

	@Override
	public List<UserVo> findAll() {
		List<UserEntity> allEntities = userRepository.findAll();
		return allEntities.stream().map(entity -> UserVoMapper.toVo(entity)).collect(Collectors.toList());
	}

}
