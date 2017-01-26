package hu.schonherz.project.admin.service.impl.user;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.interceptor.Interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ejb.interceptor.SpringBeanAutowiringInterceptor;

import hu.schonherz.project.admin.data.dao.TestDao;
import hu.schonherz.project.admin.data.entity.TestEntity;
import hu.schonherz.project.admin.service.api.service.UserServiceLocal;
import hu.schonherz.project.admin.service.api.service.UserServiceRemote;
import hu.schonherz.project.admin.service.api.vo.TestUserVo;
import hu.schonherz.project.admin.service.mapper.user.TestUserVoMapper;
import hu.schonherz.project.admin.service.user.TestUserService;

@Stateless(mappedName = "UserService")
@Remote(UserServiceRemote.class)
@Local(UserServiceLocal.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TestUserServiceBean implements TestUserService {
	@Autowired
	TestDao testDao;

	public TestUserVo findByUsername(String username) {
		TestEntity test = testDao.findByUsername(username);
		return TestUserVoMapper.toVo(test);
	}

	public TestUserVo registrationUser(TestUserVo userVo) {
		TestEntity test = TestUserVoMapper.toEntity(userVo);
		test = testDao.save(test);
		return TestUserVoMapper.toVo(test);
	}
}
