package hu.schonherz.project.admin.data.dao;

import hu.schonherz.project.admin.data.entity.TestEntity;
import java.util.List;

public interface TestDao {

    void save(TestEntity entity);

    List<TestEntity> findAll();

    TestEntity findByUsername(String username);

}
