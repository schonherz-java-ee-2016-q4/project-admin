package hu.schonherz.project.admin.data.repository;

import hu.schonherz.project.admin.data.entity.LoginEntity;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<LoginEntity, Long> {

    LoginEntity findById(Long id);

    List<LoginEntity> findByAgentId(Long agentId);

    List<LoginEntity> findByLoginDate(LocalDateTime loginDate);
}
