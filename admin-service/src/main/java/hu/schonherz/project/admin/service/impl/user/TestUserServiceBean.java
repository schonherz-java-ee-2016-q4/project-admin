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
import java.util.List;
import java.util.stream.Collectors;

@Stateless(mappedName = "UserService")
@Remote(UserServiceRemote.class)
@Local(UserServiceLocal.class)
@Interceptors(SpringBeanAutowiringInterceptor.class)
public class TestUserServiceBean implements UserServiceLocal, UserServiceRemote {

    @Autowired
    TestDao testDao;

    @Override
    public TestUserVo findByUsername(String username) {
        TestEntity test = testDao.findByUsername(username);
        return TestUserVoMapper.toVo(test);
    }

    @Override
    public TestUserVo registrationUser(TestUserVo userVo) {
        TestEntity test = TestUserVoMapper.toEntity(userVo);
        test = testDao.save(test);
        return TestUserVoMapper.toVo(test);
    }

    @Override
    public List<TestUserVo> findAll() {
        List<TestEntity> allEntities = testDao.findAll();
        return allEntities.stream()
                .map(entity -> TestUserVoMapper.toVo(entity))
                .collect(Collectors.toList());
    }

}
