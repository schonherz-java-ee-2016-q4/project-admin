package hu.schonherz.project.admin.data.dao;

import hu.schonherz.project.admin.data.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.SUPPORTS)
public interface TestDao extends JpaRepository<TestEntity, Long> {

    TestEntity findByUsername(String username);

}
