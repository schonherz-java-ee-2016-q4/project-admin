package hu.schonherz.project.admin.service.mapper.user;

import hu.schonherz.project.admin.data.entity.TestEntity;
import hu.schonherz.project.admin.service.api.vo.TestUserVo;
import lombok.NonNull;

public class TestUserVoMapper {

	public static TestUserVo toVo(@NonNull TestEntity test) {
		TestUserVo testUser = new TestUserVo();
		testUser.setId(test.getId());
		testUser.setUsername(test.getUsername());
		testUser.setEmail(test.getEmail());
		testUser.setPassword(test.getPassword());
		return testUser;
	}

	public static TestEntity toEntity(@NonNull TestUserVo userVO) {
		TestEntity testEntity = new TestEntity();
		testEntity.setId(userVO.getId());
		testEntity.setUsername(userVO.getUsername());
		testEntity.setEmail(userVO.getEmail());
		testEntity.setPassword(userVO.getPassword());
		
		return testEntity;
	}
}
